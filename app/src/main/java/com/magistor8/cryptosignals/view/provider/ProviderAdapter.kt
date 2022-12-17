package com.magistor8.cryptosignals.view.provider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.magistor8.cryptosignals.R
import com.magistor8.cryptosignals.databinding.ProviderCardBinding
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType


class ProviderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback: DiffUtil.ItemCallback<ProviderData> = object: DiffUtil.ItemCallback<ProviderData>() {
        override fun areItemsTheSame(oldSignal: ProviderData, newSignal: ProviderData): Boolean {
            return  oldSignal == newSignal
        }

        override fun areContentsTheSame(oldSignal: ProviderData, newSignal: ProviderData): Boolean {
            return  oldSignal.logo == newSignal.logo &&
                    oldSignal.id == newSignal.id &&
                    oldSignal.signals30 == newSignal.signals30 &&
                    oldSignal.signalsAll == newSignal.signalsAll &&
                    oldSignal.earn7 == newSignal.earn7 &&
                    oldSignal.earn30 == newSignal.earn30 &&
                    oldSignal.earnAll == newSignal.earnAll &&
                    oldSignal.name == newSignal.name &&
                    oldSignal.registered == newSignal.registered &&
                    oldSignal.access == newSignal.access &&
                    oldSignal.subPeriod == newSignal.subPeriod
        }

    }

    private val dataList: AsyncListDiffer<ProviderData> = AsyncListDiffer(this, diffCallback)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderAdapter.ProviderAdapterViewHolder {
        context = parent.context
        return ProviderAdapterViewHolder(ProviderCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProviderAdapterViewHolder) {
            holder.bind(dataList.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.currentList.size
    }

    fun submitList(nData: List<ProviderData>) {
        //Особенность AsyncListDiffer, который не верно сравнивает объекты
        dataList.submitList(nData.map { it.copy() })
    }

    fun getData() : List<ProviderData> = dataList.currentList

    inner class ProviderAdapterViewHolder(private val binding: ProviderCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: ProviderData) {
            if(layoutPosition != RecyclerView.NO_POSITION) {
                binding.image.load(data.logo)
                binding.username.text = data.name
                binding.time.text = "registered ${data.registered} days ago"
                binding.pay7Res.text = data.earn7.toString() + "%"
                binding.pay30Res.text = data.earn30.toString() + "%"
                binding.payallRes.text = data.earnAll.toString() + "%"
                binding.signalsResult.text = data.signals30.toString() + "/" + data.signalsAll.toString()
                if(data.access) {
                    binding.sub.visibility = GONE
                    binding.subPeriod.text = "you subscribed before ${data.subPeriod}"
                } else {
                    binding.subPeriod.visibility = GONE
                }
            }
        }
    }


}