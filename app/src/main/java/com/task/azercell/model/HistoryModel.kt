package com.task.azercell.model

import com.task.azercell.R


enum class HistoryDrawable(val drawableResId: Int) {
    FOOD(R.drawable.ic_food),
    HEALTH(R.drawable.ic_health),
    RECEIVE(R.drawable.ic_receive_transfer),
    TOPUP(R.drawable.ic_emoney_topup),
    TRANSFER(R.drawable.ic_transfer)

}


data class HistoryModel (
    val amount:String,
    val name:String,
    val date:String,
    val icon:HistoryDrawable )



val mockHistoryData: ArrayList<HistoryModel> = arrayListOf(
    HistoryModel("$12.50", "Burger King", "04.2023", HistoryDrawable.FOOD),
    HistoryModel("- $36.00", "Friend C", "01.2023", HistoryDrawable.TRANSFER),
    HistoryModel("$35.00", "Gym Membership", "02.2023", HistoryDrawable.HEALTH),
    HistoryModel("+ $50.00", "PayPal", "01.2023", HistoryDrawable.RECEIVE),
    HistoryModel("$100.00", "Bank Transfer", "03.2023", HistoryDrawable.TOPUP),
    HistoryModel("$27.99", "Pharmacy", "02.2023", HistoryDrawable.HEALTH),
    HistoryModel("$19.00", "Pizza Hut", "04.2023", HistoryDrawable.FOOD),
    HistoryModel("- $47.00", "Friend D", "04.2023", HistoryDrawable.TRANSFER),
    HistoryModel("$75.00", "Mobile Top-up", "01.2023", HistoryDrawable.TOPUP),
    HistoryModel("$42.00", "Dentist Appointment", "03.2023", HistoryDrawable.HEALTH),
    HistoryModel("$62.50", "Subway", "03.2023", HistoryDrawable.FOOD),
    HistoryModel("- $28.00", "Friend A", "04.2023", HistoryDrawable.TRANSFER),
    HistoryModel("$21.00", "Doctor Visit", "01.2023", HistoryDrawable.HEALTH),
    HistoryModel("+ $40.00", "Cash App", "04.2023", HistoryDrawable.RECEIVE),
    HistoryModel("$120.00", "Electricity Top-up", "02.2023", HistoryDrawable.TOPUP),
    HistoryModel("$13.50", "KFC", "03.2023", HistoryDrawable.FOOD),
    HistoryModel("$85.00", "Gas Top-up", "01.2023", HistoryDrawable.TOPUP),
    HistoryModel("- $18.00", "Friend B", "02.2023", HistoryDrawable.TRANSFER),
    HistoryModel("+ $80.00", "Venmo", "03.2023", HistoryDrawable.RECEIVE),
    HistoryModel("$25.00", "Water Top-up", "02.2023", HistoryDrawable.TOPUP)
)
