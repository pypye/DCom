package com.example.dcom.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.common.BaseView
import com.example.dcom.presentation.main.communication.CommunicationFragment
import com.example.dcom.presentation.main.favorite.FavoriteFragment
import com.example.dcom.presentation.main.history.HistoryFragment
import com.example.dcom.presentation.main.setting.SettingFragment
import com.example.dcom.presentation.widget.CustomViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BaseView {

    private lateinit var cvpHomePager: CustomViewPager
    private lateinit var bnvMenu: BottomNavigationView

    private lateinit var pagerAdapter: MainViewPagerAdapter
    private val fragmentList = mutableListOf<BaseFragment>()
    private var communicationFragment = CommunicationFragment()
    private var favoriteFragment = FavoriteFragment()
    private var historyFragment = HistoryFragment()
    private var settingFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        onInitView()

        initBottomNavigation()
    }

    override fun onInitView() {
        setUpVariables()
        initViewPager()
        initBottomNavigation()
    }

    private fun setUpVariables() {
        cvpHomePager = findViewById(R.id.cvpMainPager)
        bnvMenu = findViewById(R.id.bnvMainMenu)
    }

    private fun initBottomNavigation() {
        bnvMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itmCommunication -> {
                    cvpHomePager.setCurrentItem(0, false)
                    true
                }
                R.id.itmFavorite -> {
                    cvpHomePager.setCurrentItem(1, false)
                    true
                }
                R.id.itmHistory -> {
                    cvpHomePager.setCurrentItem(2, false)
                    true
                }
                else -> throw IllegalArgumentException("Invalid item id")
            }
        }
    }

    private fun initViewPager() {
        pagerAdapter = MainViewPagerAdapter(supportFragmentManager)

        fragmentList.add(communicationFragment)
        fragmentList.add(favoriteFragment)
        fragmentList.add(historyFragment)
        fragmentList.add(settingFragment)
        pagerAdapter.addListFragment(fragmentList)

        cvpHomePager.apply {
            setPagingEnabled(false)
            adapter = pagerAdapter
            offscreenPageLimit = pagerAdapter.count
            currentItem = 0
        }
    }
}
