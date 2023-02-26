package com.acxdev.poolguardapps.ui.gpu

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acxdev.commonFunction.util.ext.useCurrentTheme
import com.acxdev.commonFunction.util.ext.view.setText
import com.acxdev.commonFunction.util.ext.view.toEditString
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.databinding.RowGpuBinding
import com.acxdev.poolguardapps.model.gpu.GPUDatabase
import com.acxdev.sqlitez.SqliteZ.Companion.sqliteZUpdate

class RowGpu(
    private val list: List<GPUDatabase>,
    private val onClick: OnClick
) :
    RecyclerView.Adapter<RowGpu.ViewHolder>() {

    val checkboxStateArray = SparseBooleanArray()
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        context.useCurrentTheme()
        return ViewHolder(RowGpuBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.bind(item)

        holder.binding.apply {
            models.text = item.model
            indec.setText(item.count.toString())
            indec.editText?.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    when {
                        s.toString().trim().isEmpty() -> {
                            indec.setText("1")
                        }
                        s.toString().toInt() < 2 -> {
                            indec.isStartIconCheckable = false
                            indec.startIconDrawable?.setTint(context.getColor(com.acxdev.shimmer.R.color.default_color))
                            indec.setStartIconOnClickListener {}
                        }
                        else -> {
                            indec.isStartIconCheckable = true
                            indec.startIconDrawable?.setTint(context.getColor(R.color.red))
                            indec.setStartIconOnClickListener {
                                val value = indec.toEditString().toInt()
                                indec.setText((value - 1).toString())
                            }
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().isNotEmpty()){
                        item.count = s.toString().toInt()
                        context.sqliteZUpdate(GPUDatabase::class.java, item)
                    }
                    if (s.toString().isNotEmpty() && s.toString().startsWith("0")) s?.clear()
                }
            })

            if(indec.toEditString().toInt() > 1){
                indec.startIconDrawable?.setTint(context.getColor(R.color.red))
            }

            indec.setEndIconOnClickListener {
                val value = indec.toEditString().toInt()
                indec.setText((value + 1).toString())
            }

            indec.setStartIconOnClickListener {
                val value = indec.toEditString().toInt()
                indec.setText((value - 1).toString())
            }
        }

        holder.itemView.setOnClickListener {
            onClick.onItemClick(item, holder.adapterPosition)
        }
    }

    override fun getItemCount() = list.size

    interface OnClick {
        fun onItemClick(item: GPUDatabase, position: Int)
    }

    inner class ViewHolder(val binding: RowGpuBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(list: GPUDatabase){
            binding.models.isChecked = checkboxStateArray.get(layoutPosition, false) || list.isChecked == 1
            binding.models.setOnClickListener {
                if(!checkboxStateArray.get(adapterPosition, false)){
                    binding.models.isChecked = true
                    checkboxStateArray.put(adapterPosition, true)
                    list.isChecked = 1
                } else {
                    binding.models.isChecked = false
                    checkboxStateArray.put(adapterPosition, false)
                    list.isChecked = 0
                }
                context.sqliteZUpdate(GPUDatabase::class.java, list)
            }
        }
    }
}

