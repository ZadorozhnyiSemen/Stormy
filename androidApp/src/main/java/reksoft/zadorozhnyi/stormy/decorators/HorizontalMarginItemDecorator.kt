package reksoft.zadorozhnyi.stormy.decorators

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import reksoft.zadorozhnyi.stormy.R

class HorizontalMarginItemDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private val margin = context.resources.getDimension(R.dimen.decor)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = margin.toInt()
        outRect.left = margin.toInt()
    }
}