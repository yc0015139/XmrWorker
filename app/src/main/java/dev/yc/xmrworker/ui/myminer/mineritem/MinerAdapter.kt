package dev.yc.xmrworker.ui.myminer.mineritem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.yc.xmrworker.databinding.ItemMinerStateBinding

class MinerAdapter :
    ListAdapter<MinerState, MinerAdapter.MinerViewHolder>(MinerStateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MinerViewHolder.create(parent)

    override fun onBindViewHolder(holder: MinerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MinerViewHolder(
        private val binding: ItemMinerStateBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(minerState: MinerState) {
            binding.apply {
                tvMinerId.text = minerState.id
                tvMinerHash.text = minerState.hash
                tvMinerTimestamp.text = minerState.lastTimeInTimestamp.toString()
                tvMinerTotalHash.text = minerState.totalHash
            }
        }

        companion object {
            fun create(parent: ViewGroup) = MinerViewHolder(
                binding = ItemMinerStateBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    class MinerStateDiffCallback : DiffUtil.ItemCallback<MinerState>() {
        override fun areItemsTheSame(oldItem: MinerState, newItem: MinerState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MinerState, newItem: MinerState): Boolean {
            return oldItem == newItem
        }
    }
}