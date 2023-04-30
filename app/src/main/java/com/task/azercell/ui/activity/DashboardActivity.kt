package com.task.azercell.ui.activity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.islamkhsh.viewpager2.ViewPager2
import com.task.azercell.CARD_ID
import com.task.azercell.R
import com.task.azercell.adapter.CardAdapter
import com.task.azercell.adapter.CustomListAdapter
import com.task.azercell.databinding.ActivityDashboardBinding
import com.task.azercell.databinding.ItemHistoryBinding
import com.task.azercell.model.CardModel
import com.task.azercell.model.ClientModel
import com.task.azercell.model.HistoryModel
import com.task.azercell.model.mockHistoryData
import com.task.azercell.ui.fragment.CardDetailsFragment
import com.task.azercell.util.Helpers.hideSoftKeyboard
import com.task.azercell.util.HistoryCallback
import com.task.azercell.util.replaceFragment
import com.task.azercell.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), CardAdapter.OnSliderItemClickListener,
    CardDetailsFragment.OnFragmentRemoved {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private lateinit var cardList: List<CardModel>
    private lateinit var currentCard: CardModel
    private lateinit var client: ClientModel

    private val viewModel: DashboardViewModel by viewModels()


    private var doubleTapExitJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setClickEvents()
        setBackPressedDispatcher()

    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        lifecycleScope.launch {
            viewModel.cardList.collect { data ->
                if (data == null) return@collect
                cardList = data
                initCardSlider()
                initHistoryRecycler()

            }
        }
        lifecycleScope.launch {
            viewModel.client.collect { data ->
                if (data == null) return@collect
                client = data
                initClientData()
            }
        }
    }

    private fun initClientData() {
        binding.apply {
            val coloredText =
                """<font color="#FFFFFF">${client.name} </font> <font color="#2567F9">${client.surname}</font>"""
            clientFullName.text = HtmlCompat.fromHtml(coloredText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun initHistoryRecycler() {
        binding.apply {
            lifecycleScope.launch {
                Log.i("aaaa", "initHistoryRecycler: ")
                binding.historyRecycler.visibility = View.INVISIBLE
                binding.shimmer.root.visibility = View.VISIBLE
                binding.shimmer.shimmerFrameLayout.startShimmerAnimation()
                val deferred = lifecycleScope.async {
                    delay(1500)
                    binding.shimmer.shimmerFrameLayout.stopShimmerAnimation()
                    binding.shimmer.root.visibility = View.GONE
                    binding.historyRecycler.visibility = View.VISIBLE
                }
                deferred.await()
                val adapter = CustomListAdapter<HistoryModel, ItemHistoryBinding>(
                    HistoryCallback(),
                    R.layout.item_history
                ) {}


                historyRecycler.layoutManager = LinearLayoutManager(applicationContext)
                historyRecycler.adapter = adapter
                adapter.submitList(mockHistoryData)
            }
        }
    }

    private fun initCardSlider() {
        binding.apply {
            val adapter = CardAdapter(cardList, this@DashboardActivity)
            cardViewPager.adapter = adapter
            cardViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentCard = cardList[position]
                    cardName.text = currentCard.cardName
                }
            })
        }
    }

    private fun setClickEvents() {
        binding.apply {
            btnAddNewCard.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, AddNewCardActivity::class.java))
            }
            btnSend.setOnClickListener {
                val intent = Intent(this@DashboardActivity, TransferActivity::class.java)
                intent.putExtra(CARD_ID, currentCard.id)
                startActivity(intent)
            }


            btnTopUp.setOnClickListener {
                Toast.makeText(this@DashboardActivity, "Try Send", Toast.LENGTH_SHORT).show()
            }
            btnPayment.setOnClickListener {

                Toast.makeText(this@DashboardActivity, "Try Send", Toast.LENGTH_SHORT).show()
            }
            btnReceive.setOnClickListener {
                Toast.makeText(this@DashboardActivity, "Try Send", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSliderItemClick(id: String) {
        val bundle = Bundle()
        bundle.putString(CARD_ID, id)
        replaceFragment(R.id.container, CardDetailsFragment(this), bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        doubleTapExitJob?.cancel()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    hideSoftKeyboard(this)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleTapExitJob == null) {
                        if (supportFragmentManager.fragments.size > 0) {
                            supportFragmentManager.beginTransaction()
                                .remove(supportFragmentManager.fragments[0])
                                .commit()
                            return
                        }
                        doubleTapExitJob = lifecycleScope.launch {
                            Toast.makeText(
                                this@DashboardActivity,
                                "Press back again to exit",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            delay(2000) // wait for 2 seconds
                            doubleTapExitJob = null
                        }
                    } else {
                        doubleTapExitJob?.cancel()
                        this@DashboardActivity.finish()
                    }
                }
            })
    }

    override fun onFragmentRemoved() {
        viewModel.loadCardList()
    }


}


