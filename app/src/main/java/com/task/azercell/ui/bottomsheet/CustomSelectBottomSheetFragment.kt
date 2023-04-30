package com.task.azercell.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.azercell.HEADER
import com.task.azercell.R
import com.task.azercell.adapter.CustomListAdapter
import com.task.azercell.databinding.FragmentSelectCardBottomSheetBinding


class CustomSelectBottomSheetFragment <T : Any, B : ViewDataBinding>(
    private val adapter: CustomListAdapter<T,B>
    ) :
    CustomBottomSheet() {

    private val binding by lazy {
        FragmentSelectCardBottomSheetBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val bundle: Bundle? = arguments

        if (bundle != null) {
            val headerText = bundle.getString(HEADER, "")

            binding.apply {
                header.text = headerText

                val dividerDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.divider)
                val dividerItemDecoration =
                    DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
                dividerItemDecoration.setDrawable(dividerDrawable!!)
                recyclerSelectBottomSheet.layoutManager = LinearLayoutManager(context)
                recyclerSelectBottomSheet.addItemDecoration(dividerItemDecoration)
                recyclerSelectBottomSheet.adapter = adapter

            }
        }

        return binding.root
    }


}