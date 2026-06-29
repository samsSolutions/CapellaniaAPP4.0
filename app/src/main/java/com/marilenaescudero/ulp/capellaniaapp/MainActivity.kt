package com.marilenaescudero.ulp.capellaniaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.marilenaescudero.ulp.capellaniaapp.databinding.ActivityMainBinding
import com.marilenaescudero.ulp.capellaniaapp.login.LoginActivity
import com.marilenaescudero.ulp.capellaniaapp.ui.inicio.InicioFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.nav_perfil,
                R.id.InicioFragment,
            ),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // HEADER DEL NAV DRAWER
        val headerView = navView.getHeaderView(0)
        val imgUser = headerView.findViewById<ImageView>(R.id.imgUser)
        val txtUserName = headerView.findViewById<TextView>(R.id.txtUserName)
        val txtUserRole = headerView.findViewById<TextView>(R.id.txtUserRole)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val nombre = sharedPref.getString("nombre", "")
        val apellido = sharedPref.getString("apellido", "")
        val rol = sharedPref.getString("rol", "") ?: ""
        val avatar = sharedPref.getString("avatar", "")
        val token = sharedPref.getString("token", "")

        configurarMenuPorRol(rol)

        Log.d("SESSION_DEBUG", "Token recibido: ${token?.take(25)}...")
        Log.d("SESSION_DEBUG", "Rol recibido (normalizado): $rol")
        Log.d("SESSION_DEBUG", "Usuario recibido: $nombre $apellido")

        // Mostrar nombre
        txtUserName.text = "$nombre $apellido"

        // Mostrar rol bonito
        txtUserRole.text = when (rol) {
            "capellan" -> "Capellán"
            "administrador" -> "Administrador"
            "personal" -> "Personal"
            "familiar" -> "Familiar"
            else -> rol.replaceFirstChar { it.uppercase() }
        }

        // Avatar
        if (!avatar.isNullOrEmpty()) {
            Glide.with(this)
                .load(avatar)
                .placeholder(R.drawable.ic_menu_camera)
                .into(imgUser)
        }

        // Navegación del menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.nav_logout -> {
                    mostrarDialogoSalir()
                    true
                }

                else -> {
                    navController.navigate(menuItem.itemId)
                    drawerLayout.closeDrawers()
                    true
                }
            }
        }
    }

    // CONFIGURAR MENÚ SEGÚN ROL NORMALIZADO
    private fun configurarMenuPorRol(rol: String?) {
        val menu = binding.navView.menu

        when (rol) {
            "administrador" -> {
                menu.findItem(R.id.nav_auditoria).isVisible = true
                menu.findItem(R.id.nav_estadisticas).isVisible = true
                menu.findItem(R.id.nav_gestion).isVisible = true
            }
            "capellan" -> {
                menu.findItem(R.id.nav_auditoria).isVisible = false
                menu.findItem(R.id.nav_estadisticas).isVisible = false
                menu.findItem(R.id.nav_gestion).isVisible = true
            }
            "personal", "familiar" -> {
                menu.findItem(R.id.nav_auditoria).isVisible = false
                menu.findItem(R.id.nav_estadisticas).isVisible = false
                menu.findItem(R.id.nav_gestion).isVisible = false
            }
        }
    }

    private fun mostrarDialogoSalir() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                val prefs = getSharedPreferences("UserData", MODE_PRIVATE)
                prefs.edit().clear().apply()

                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

