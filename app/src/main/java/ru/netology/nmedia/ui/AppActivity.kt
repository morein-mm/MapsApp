package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import ru.netology.nmedia.R

class AppActivity : AppCompatActivity(R.layout.activity_app) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)

//                menu.let {
//                    menu.findItem(R.id.action_placemarks_list).setVisible(supportFragmentManager.fragments.last()?.getChildFragmentManager()?.fragments?.get(0) is MapsFragment)
//                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.action_placemarks_list -> {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.action_mapsFragment_to_placemarksFragment,
                            )
                        true
                    }

                    else -> false
                }

        })

    }
}