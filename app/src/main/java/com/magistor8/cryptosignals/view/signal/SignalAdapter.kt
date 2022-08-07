package com.magistor8.cryptosignals.view.signal

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.magistor8.cryptosignals.R
import com.magistor8.cryptosignals.databinding.SignalCardBinding
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType


class SignalAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback: DiffUtil.ItemCallback<SignalData> = object: DiffUtil.ItemCallback<SignalData>() {
        override fun areItemsTheSame(oldSignal: SignalData, newSignal: SignalData): Boolean {
            return  oldSignal == newSignal
        }

        override fun areContentsTheSame(oldSignal: SignalData, newSignal: SignalData): Boolean {
            return  oldSignal.pair == newSignal.pair &&
                    oldSignal.type == newSignal.type &&
                    oldSignal.providerId == newSignal.providerId &&
                    oldSignal.providerName == newSignal.providerName &&
                    oldSignal.priceOpen == newSignal.priceOpen &&
                    oldSignal.priceClose == newSignal.priceClose &&
                    oldSignal.target1 == newSignal.target1 &&
                    oldSignal.target2 == newSignal.target2 &&
                    oldSignal.target3 == newSignal.target3 &&
                    oldSignal.access == newSignal.access
        }
    }

    private val dataList: AsyncListDiffer<SignalData> = AsyncListDiffer(this, diffCallback)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignalAdapter.SignalAdapterViewHolder {
        context = parent.context
        return SignalAdapterViewHolder(SignalCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SignalAdapterViewHolder) {
            holder.bind(dataList.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.currentList.size
    }

    fun submitList(nData: List<SignalData>) {
        //Особенность AsyncListDiffer, который не верно сравнивает объекты
        dataList.submitList(nData.map { it.copy() })
    }

    inner class SignalAdapterViewHolder(private val binding: SignalCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: SignalData) {
            if(layoutPosition != RecyclerView.NO_POSITION) {
                binding.image.load(data.logo)
                binding.title.text = data.pair
                signalType(data.type)
                binding.providerUsername.text = data.providerName
                showContent(data)
            }
        }

        private fun showContent(data: SignalData) {
            if (data.access) {
                binding.openPrice.text = context.getString(R.string.open) + data.priceOpen
                binding.closePrice.text = context.getString(R.string.close) + data.priceClose
                binding.t1p.text = data.target1.toString()
                binding.t2p.text = data.target2.toString()
                binding.t3p.text = data.target3.toString()
                binding.openPrice.visibility = VISIBLE
                binding.closePrice.visibility = VISIBLE
                binding.t1.visibility = VISIBLE
                binding.t2.visibility = VISIBLE
                binding.t3.visibility = VISIBLE
                binding.t1p.visibility = VISIBLE
                binding.t2p.visibility = VISIBLE
                binding.t3p.visibility = VISIBLE
                binding.currentPrice.visibility = VISIBLE
                binding.percent.visibility = VISIBLE
                binding.greyLine.visibility = VISIBLE
                binding.subButton.visibility = GONE
            } else {
                binding.openPrice.visibility = GONE
                binding.closePrice.visibility = GONE
                binding.t1.visibility = GONE
                binding.t2.visibility = GONE
                binding.t3.visibility = GONE
                binding.t1p.visibility = GONE
                binding.t2p.visibility = GONE
                binding.t3p.visibility = GONE
                binding.currentPrice.visibility = GONE
                binding.percent.visibility = GONE
                binding.greyLine.visibility = GONE
                binding.subButton.visibility = VISIBLE
            }
        }

        private fun signalType(type: SignalType) {
            when (type) {
                is SignalType.Spot -> {
                    val color = ContextCompat.getColorStateList(context, R.color.forest)
                    binding.typeSignalB.backgroundTintList = color
                    binding.typeSignalT.text = context.getString(R.string.spot)
                }
                is SignalType.FutureLong -> {
                    val color = ContextCompat.getColorStateList(context, R.color.t_blue)
                    binding.typeSignalB.backgroundTintList = color
                    binding.typeSignalT.text = context.getString(R.string.longs) + type.x
                }
                is SignalType.FutureShort -> {
                    val color = ContextCompat.getColorStateList(context, R.color.t_pink)
                    binding.typeSignalB.backgroundTintList = color
                    binding.typeSignalT.text = context.getString(R.string.shorts) + type.x
                }
            }
        }
    }


}