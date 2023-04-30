package com.task.azercell.ui.bottomsheet


import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.task.azercell.R

@Suppress("DEPRECATION")
open class CustomBottomSheet : BottomSheetDialogFragment() {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<FrameLayout>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        showBottomSheet()
        return bottomSheetDialog
    }

    private fun showBottomSheet() {
        bottomSheetDialog.setOnShowListener {
            try {
                val bottomSheet =
                    bottomSheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                val displayMetrics = DisplayMetrics()

                activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

                bottomSheet?.let {
                    val params = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
                    bottomSheet.setBackgroundColor(resources.getColor(R.color.transparent))

                    params.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
                    bottomSheet.layoutParams = params
                    behavior = BottomSheetBehavior.from(bottomSheet)
                    behavior.skipCollapsed = true
                    behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
                }

            } catch (ignored: Exception) {
            }
        }
    }
}
