package dev.yc.xmrworker.ui.myminer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dev.yc.xmrworker.databinding.MinerStatesFragmentBinding
import dev.yc.xmrworker.ui.myminer.mineritem.MinerAdapter
import dev.yc.xmrworker.utils.livedata.EventObserver

class MinersFragment : Fragment() {
    private var _binding: MinerStatesFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MinersViewModel by viewModels()

    private val adapter = MinerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = MinerStatesFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupEvents()
        setupObservers()
        setupViews()
    }

    private fun setupEvents() {
        viewModel.errorEvent.observe(viewLifecycleOwner, EventObserver {
            view?.let {
                Snackbar.make(it, "Something wrong when loading data", Snackbar.LENGTH_LONG).show()
            }
        })

        viewModel.refreshedEvent.observe(viewLifecycleOwner, EventObserver {
            binding.srlFetchMiners.isRefreshing = false
        })
    }

    private fun setupObservers() {
        viewModel.minerData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.empty.observe(viewLifecycleOwner) { isEmpty ->
            binding.rvMiners.isVisible = !isEmpty
            binding.tvEmpty.isVisible = isEmpty
        }
    }

    private fun setupViews() {
        setupRefresh()
        setupMiners()
    }

    private fun setupRefresh() {
        binding.srlFetchMiners.setOnRefreshListener {
            viewModel.doRefresh()
        }
    }

    private fun setupMiners() {
        binding.rvMiners.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
