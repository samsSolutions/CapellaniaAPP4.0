package com.marilenaescudero.ulp.capellaniaapp.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.marilenaescudero.ulp.capellaniaapp.MainActivity
import com.marilenaescudero.ulp.capellaniaapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val correo = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            viewModel.login(correo, password)
        }

        observeLogin()
    }

    private fun observeLogin() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {

                    is LoginState.Success -> {
                        binding.btnLogin.text = "Ingresar"
                        val user = state.data
                        
                        if (user.error != null) {
                            when (user.error) {
                                "UsuarioNoExiste" ->
                                    Toast.makeText(this@LoginActivity, "El usuario no existe", Toast.LENGTH_SHORT).show()

                                "UsuarioInactivo" ->
                                    Toast.makeText(this@LoginActivity, "El usuario está dado de baja", Toast.LENGTH_SHORT).show()

                                "PasswordIncorrecta" ->
                                    Toast.makeText(this@LoginActivity, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                            }
                            return@collect
                        }


                        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

                        val rolNormalizado = user.rol
                            .trim()
                            .lowercase()
                            .replace("á", "a")
                            .replace("é", "e")
                            .replace("í", "i")
                            .replace("ó", "o")
                            .replace("ú", "u")

                        with(sharedPref.edit()) {
                            putString("nombre", user.nombre)
                            putString("apellido", user.apellido)
                            putString("correo", user.correo)
                            putString("rol", rolNormalizado)
                            putString("avatar", user.avatar ?: "")
                            putString("token", user.token)
                            putInt("idUsuario", user.idUsuario)
                            apply()
                        }

                        Log.d("LOGIN_DEBUG", "Token guardado: ${user.token.take(25)}...")
                        Log.d("LOGIN_DEBUG", "Rol guardado: ${user.rol}")
                        Log.d("LOGIN_DEBUG", "Usuario guardado: ${user.nombre} ${user.apellido}")
                        Log.d("LOGIN_DEBUG", "ID Usuario guardado: ${user.idUsuario}")


                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }

                    else -> Unit
                }
            }
        }
    }
}

