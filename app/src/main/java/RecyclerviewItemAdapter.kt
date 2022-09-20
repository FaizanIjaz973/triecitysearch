import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.triecitysearch.R
import com.example.triecitysearch.model.CoordClass

class RecyclerviewItemAdapter internal constructor(private val itemsList: List<Items>) :
    RecyclerView.Adapter<RecyclerviewItemAdapter.MyViewHolder>() {
    private var clickListener: ClickListener<Items>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.name.text = item.name
        holder.price.text = item.price.toString()
        holder.itemLayout.setOnClickListener { v -> clickListener!!.onClick(v, item, position) }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun setOnItemClickListener(clickListener: ClickListener<Items>) {
        this.clickListener = clickListener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.tvName)
        var price: TextView = itemView.findViewById(R.id.tvPrice)
        val itemLayout: CardView = itemView.findViewById(R.id.itemLayout)
    }
}

interface ClickListener<T> {
    fun onClick(view: View?, data: T, position: Int)
}

data class Items internal constructor(var name: String, var price: Int, var coord: CoordClass)