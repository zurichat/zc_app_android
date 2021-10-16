package com.zurichat.app.util.jsearch_view_utils

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.trimmedLength
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.zurichat.app.R
import com.zurichat.app.databinding.ChannelsListItemBinding
import com.zurichat.app.databinding.ItemChatBinding
import com.zurichat.app.databinding.SearchviewLayoutBinding
import com.zurichat.app.models.ChannelModel
import com.zurichat.app.models.SearchItem
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import androidx.room.Room
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.zurichat.app.R
import com.zurichat.app.databinding.SearchviewLayoutBinding
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.fragments.home_screen.adapters.ChatsAdapter
import com.zurichat.app.util.jsearch_view_utils.*
import java.text.SimpleDateFormat
import java.util.*

class JSearchView @JvmOverloads constructor(
    creationContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(creationContext, attrs, defStyleAttr) {

    /**
     * @param animationDuration duration, in ms, of the reveal animations
     * @return current reveal or fade animations duration
     */

    var animationDuration = ANIMATION_DURATION_DEFAULT

    /**
     * @param revealAnimationCenter center of the reveal animation, used to customize the origin of the animation
     * @return center of the reveal animation, by default it is placed where the rightmost MenuItem would be
     */
    var revealAnimationCenter: Point? = null
        get() {
            if (field != null) {
                return field
            }

            val centerX = width - convertDpToPx(ANIMATION_CENTER_PADDING, context)
            val centerY = height / 2

            field = Point(centerX, centerY)
            return field
        }
    private var query: CharSequence? = null
    private var oldQuery: CharSequence? = null
    var isSearchOpen = false
        private set
    private var isClearingFocus = false


    /**
     * @return the TabLayout attached to the JSearchView behavior
     */
    var tabLayout: TabLayout? = null
        private set
    private var tabLayoutInitialHeight = 0
    private var onQueryChangeListener: OnQueryTextListener? = null
    private var searchViewListener: SearchViewListener? = null
    private var searchIsClosing = false
    private var keepQuery = false

    val adapter = SearchResultsAdapter()

    private val binding = SearchviewLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    private fun initSearchEditText() = with(binding) {
        searchEditText.doOnTextChanged { text, start, before, count ->
            if (!searchIsClosing) {
                if (text != null) {
                    this@JSearchView.onTextChanged(text)
                }
            }
        }

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                showKeyboard(searchEditText)
            }
            else{
                closeSearch()
            }
        }

        searchEditText.setOnEditorActionListener { _: TextView?, _: Int, _: KeyEvent? ->
            onSubmitQuery()
            true
        }

    }
    fun addChipToGroup(text: String){
        val chip = Chip(context)
        chip.text = text
        //chip.chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_remove_icon)
        chip.isChipIconVisible = false
        chip.isCheckedIconVisible = true
        // to get single selection working
        chip.isClickable = true
        chip.setTextAppearance(R.style.TextAppearance_AppCompat_Inverse)
        chip.setCloseIconTintResource(R.color.white)
        chip.isCheckable = false
        chip.isCloseIconVisible = true
        chip.setChipBackgroundColorResource(R.color.background_color)
        binding.searchChipLayout.addView(chip as View)
        chip.setOnCloseIconClickListener {
            binding.searchChipLayout.removeView(chip as View)
            binding.chipGroup.visibility = VISIBLE
        }
        binding.chipGroup.visibility = GONE
    }

    fun chipListener(chip:Chip) = with(binding){
        chip.setOnClickListener {
            val text: String = chip.text.toString()
            addChipToGroup(text)
        }
    }
    private fun initClickListeners() = with(binding) {
        backButton.setOnClickListener { closeSearch() }
        //clearButton.setOnClickListener { clearSearch() }
        chipListener(audioChip)
        chipListener(videosChip)
        chipListener(docsChip)
        chipListener(photosChip)
        chipListener(linksChip)
    }

    override fun clearFocus() = with(binding) {
        isClearingFocus = true
        hideKeyboard(this@JSearchView)
        super.clearFocus()
        searchEditText.clearFocus()
        isClearingFocus = false
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean = with(binding) {
            if (isClearingFocus) {
                return false
            }
            return if (!isFocusable) {
                false
            } else searchEditText.requestFocus(direction, previouslyFocusedRect)
        }

    public override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.query = if (query != null) query.toString() else null
        savedState.isSearchOpen = isSearchOpen
        savedState.animationDuration = animationDuration
        savedState.keepQuery = keepQuery
        return savedState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        query = state.query
        animationDuration = state.animationDuration
        keepQuery = state.keepQuery
        if (state.isSearchOpen) {
            showSearch(false)
            setQuery(state.query, false)
        }
        super.onRestoreInstanceState(state.superState)
    }

    private fun clearSearch() = with(binding) {
        searchEditText.text = null
        onQueryChangeListener?.onQueryTextCleared()
    }

    private fun onTextChanged(newText: CharSequence) = with(binding) {
        query = newText
        val hasText = newText.isNotBlank()
        if (hasText) {
            //clearButton.visibility = VISIBLE
            searchRv.visibility = VISIBLE
        } else {
            //clearButton.visibility = GONE
            searchRv.visibility = GONE
        }
        if (newText != oldQuery) {
            onQueryChangeListener?.onQueryTextChange(newText.toString())
        }
        oldQuery = newText.toString()
    }

    private fun onSubmitQuery() = with(binding) {
        val submittedQuery: CharSequence? = searchEditText.text
        if (submittedQuery != null && submittedQuery.trimmedLength() > 0) {
            if (onQueryChangeListener == null || !onQueryChangeListener!!.onQueryTextSubmit(
                    submittedQuery.toString()
                )
            ) {
//                closeSearch()
//                searchIsClosing = true
//                searchEditText.text = null
//                searchIsClosing = false
            }
        }
    }


    /**
     * Shows search with animation
     * @param animate true to animate
     */
    @JvmOverloads
    fun showSearch(animate: Boolean = true) = with(binding) {
        if (isSearchOpen) {
            return null
        }
        searchEditText.setText(if (keepQuery) query else null)
        searchEditText.requestFocus()
        chipGroup.visibility = VISIBLE
        if (animate) {
            val animationListener: AnimationListener = object : JAnimationListener() {
                override fun onAnimationEnd(view: View): Boolean {
                    searchViewListener?.onSearchViewShownAnimation()
                    return false
                }
            }
            revealView(
                this@JSearchView,
                animationDuration,
                animationListener,
                null//revealAnimationCenter
            ).start()
        } else {
            visibility = VISIBLE
        }
        hideTabLayout(animate)
        isSearchOpen = true
        searchViewListener?.onSearchViewShown()
    }


    @JvmOverloads
    fun closeSearch(animate: Boolean = true) = with(binding) {
        if (!isSearchOpen) {
            return null
        }
        searchIsClosing = true
        searchEditText.text = null
        searchRv.visibility = GONE
        searchIsClosing = false
        chipGroup.visibility = GONE
        clearFocus()
        if (animate) {
            val animationListener: AnimationListener = object : JAnimationListener() {
                override fun onAnimationEnd(view: View): Boolean {
                    searchViewListener?.onSearchViewClosedAnimation()
                    return false
                }
            }
            hideView(
                this@JSearchView,
                animationDuration,
                animationListener,
                null//revealAnimationCenter
            ).start()
        } else {
            visibility = GONE
        }
        showTabLayout(animate)
        isSearchOpen = false
        searchViewListener?.onSearchViewClosed()
    }

    /**
     * Sets a TabLayout that is automatically hidden when the search opens, and shown when the search closes
     */
    fun setTabLayout(tabLayout: TabLayout) {
        this.tabLayout = tabLayout
        this.tabLayout!!.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                tabLayoutInitialHeight = tabLayout.height
                tabLayout.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
        this.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit

            override fun onTabUnselected(tab: TabLayout.Tab) {
                closeSearch()
            }
        })
    }

    /**
     * Shows the attached TabLayout with animation
     *
     * @param animate true if should be animated
     */
    @JvmOverloads
    fun showTabLayout(animate: Boolean = true) {
        if (tabLayout == null) {
            return
        }

        if (animate) {
            verticalSlideView(tabLayout!!, 0, tabLayoutInitialHeight, animationDuration).start()
        } else {
            tabLayout?.visibility = VISIBLE
        }
    }

    /**
     * Hides the attached TabLayout with animation
     *
     * @param animate true if should be animated
     */
    @JvmOverloads
    fun hideTabLayout(animate: Boolean = true) {
        if (tabLayout == null) {
            return
        }

        if (animate) {
            verticalSlideView(tabLayout!!, tabLayout!!.height, 0, animationDuration).start()
        } else {
            tabLayout!!.visibility = GONE
        }
    }

    /**
     * Call this method on the onBackPressed method of the activity.
     * Returns true if the search was open and it closed with the call.
     * Returns false if the search was already closed and can continue with the default activity behavior.
     *
     * @return true if acted, false if not acted
     */
    fun onBackPressed(): Boolean {
        if (isSearchOpen) {
            closeSearch()
            return true
        }
        return false
    }


    /**
     * Sets icons alpha, does not set the back/up icon
     */
    fun setIconsAlpha(alpha: Float) = with(binding) {
        //clearButton.alpha = alpha
    }

    /**
     * @param sequence  query text
     * @param submit true to submit the query
     */
    fun setQuery(sequence: CharSequence?, submit: Boolean) = with(binding) {
        searchEditText.setText(sequence)
        if (sequence != null) {
            searchEditText.setSelection(searchEditText.length())
            query = sequence
        }
        if (submit && !sequence.isNullOrEmpty()) {
            onSubmitQuery()
        }
    }

    /**
     * Handle click events for the MenuItem.
     *
     * @param menuItem MenuItem that opens the search
     */
    fun setMenuItem(menuItem: MenuItem) {
        menuItem.setOnMenuItemClickListener {
            showSearch()
            true
        }
    }

    /**
     * @param listener listens to query changes
     */
    fun setOnQueryTextListener(listener: OnQueryTextListener?) {
        onQueryChangeListener = listener
    }

    /**
     * Set this listener to listen to search open and close events
     *
     * @param listener listens to JSearchView opening, closing, and the animations end
     */
    fun setOnSearchViewListener(listener: SearchViewListener?) {
        searchViewListener = listener
    }

    fun handleResults(searchResult: List<SearchItem<RoomsListResponseItem, ChannelModel>>) {

        adapter.differ.submitList(searchResult)

    }


    internal class SavedState : BaseSavedState {
        var query: String? = null
        var isSearchOpen = false
        var animationDuration = 0
        var voiceSearchPrompt: String? = null
        var keepQuery = false

        constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            query = `in`.readString()
            isSearchOpen = `in`.readInt() == 1
            animationDuration = `in`.readInt()
            voiceSearchPrompt = `in`.readString()
            keepQuery = `in`.readInt() == 1
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(query)
            out.writeInt(if (isSearchOpen) 1 else 0)
            out.writeInt(animationDuration)
            out.writeString(voiceSearchPrompt)
            out.writeInt(if (keepQuery) 1 else 0)
        }

        companion object {
            //required field that makes Parcelables from a Parcel
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState?> =
                object : Parcelable.Creator<SavedState?> {
                    override fun createFromParcel(`in`: Parcel): SavedState? {
                        return SavedState(`in`)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    interface OnQueryTextListener {
        /**
         * @param query the query text
         * @return true to override the default action
         */
        fun onQueryTextSubmit(query: String): Boolean

        /**
         * @param newText the query text
         * @return true to override the default action
         */
        fun onQueryTextChange(newText: String): Boolean

        /**
         * Called when the query text is cleared by the user.
         *
         * @return true to override the default action
         */
        fun onQueryTextCleared(): Boolean
    }

    interface SearchViewListener {
        /**
         * Called instantly when the search opens
         */
        fun onSearchViewShown()

        /**
         * Called instantly when the search closes
         */
        fun onSearchViewClosed()

        /**
         * Called at the end of the show animation
         */
        fun onSearchViewShownAnimation()

        /**
         * Called at the end of the close animation
         */
        fun onSearchViewClosedAnimation()
    }

    companion object {
        const val REQUEST_VOICE_SEARCH = 735
        const val CARD_CORNER_RADIUS = 4
        const val ANIMATION_CENTER_PADDING = 26
        private const val CARD_PADDING = 6
        private const val CARD_ELEVATION = 2
        private const val BACK_ICON_ALPHA_DEFAULT = 0.87f
        private const val ICONS_ALPHA_DEFAULT = 0.54f
        const val STYLE_BAR = 0
        const val STYLE_CARD = 1
    }

    init {
        initSearchEditText()
        initClickListeners()
        if (!isInEditMode) {
            visibility = GONE
        }
        binding.searchRv.adapter = adapter
        val rvHeight = screenRectPx.height()
        binding.searchRv.layoutParams.height = rvHeight
        binding.searchRv.requestLayout()
    }

    fun setClickListeners(rvAdapter: (SearchResultsAdapter)->Unit){
        rvAdapter(adapter)
    }
}

class SearchResultsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var onRoomItemClickListener: ((room:RoomsListResponseItem) -> Unit)? = null

    fun setRoomItemClickListener(listener: (room:RoomsListResponseItem) -> Unit) {
        onRoomItemClickListener = listener
    }


    private var onChannelItemClickListener: ((channel:ChannelModel) -> Unit)? = null

    fun setChannelItemClickListener(listener: (channel:ChannelModel) -> Unit) {
        onChannelItemClickListener = listener
    }


    inner class ChannelsVH(val binding: ChannelsListItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(channel: ChannelModel) {
            binding.channelTitle.text = channel.name
            binding.root.setOnClickListener{
                onChannelItemClickListener?.invoke(channel)
            }

            if (channel.isPrivate){
                binding.fab.setImageDrawable(ContextCompat.getDrawable( binding.fab.context,R.drawable.ic_new_lock))

            }else{
                binding.fab.setImageDrawable(ContextCompat.getDrawable( binding.fab.context,R.drawable.ic_hash))

            }
        }

    }

    inner class RoomsVH(val binding: ItemChatBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(room: RoomsListResponseItem) {
            binding.textChatUsername.text = room.room_name
            binding.root.setOnClickListener{
                onRoomItemClickListener?.invoke(room)
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<SearchItem<RoomsListResponseItem, ChannelModel>>() {
        override fun areItemsTheSame(oldItem: SearchItem<RoomsListResponseItem, ChannelModel>, newItem: SearchItem<RoomsListResponseItem, ChannelModel>): Boolean {
            return if (oldItem.room != null) {
                oldItem.room._id == newItem.room?._id
            } else {
                oldItem.channel?._id == newItem.channel?._id
            }
        }

        override fun areContentsTheSame(oldItem: SearchItem<RoomsListResponseItem, ChannelModel>, newItem: SearchItem<RoomsListResponseItem, ChannelModel>): Boolean {
            return if (oldItem.room != null) {
                oldItem.room == newItem.room
            } else {
                oldItem.channel == newItem.channel
            }
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val roomBinding = ItemChatBinding.inflate(inflater, parent, false)
        val channelBinding = ChannelsListItemBinding.inflate(inflater, parent, false)

        return if (viewType == ROOM_VIEW_TYPE) {
            RoomsVH(roomBinding)
        } else {
            ChannelsVH(channelBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        when (holder.itemViewType) {
            ROOM_VIEW_TYPE -> {
                item.room?.let { (holder as RoomsVH).bind(it) }
            }
            CHANNEL_VIEW_TYPE -> {
                item.channel?.let { (holder as ChannelsVH).bind(it) }
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is SearchItem.Room<RoomsListResponseItem, ChannelModel> -> ROOM_VIEW_TYPE
            is SearchItem.Channel<RoomsListResponseItem, ChannelModel> -> CHANNEL_VIEW_TYPE
        }
    }

    private companion object {
        private const val ROOM_VIEW_TYPE = 0
        private const val CHANNEL_VIEW_TYPE = 1
    }
}