package com.marilenaescudero.ulp.capellaniaapp.ui.palabraDia


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.marilenaescudero.ulp.capellaniaapp.databinding.FragmentPalabraDiaBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener



class PalabraDiaFragment : Fragment() {

    private var _binding: FragmentPalabraDiaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PalabraDiaViewModel by viewModels {
        PalabraDiaViewModelFactory(requireContext())
    }

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPalabraDiaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.palabra.observe(viewLifecycleOwner) { palabra ->
            if (palabra != null) {
                binding.txtVersiculo.text = palabra.versiculo ?: ""
                binding.txtReflexion.text = palabra.reflexion ?: ""
                binding.txtFecha.text = palabra.fecha ?: ""

                mostrarMultimedia(palabra.multimedia ?: "")
            } else {
                binding.txtVersiculo.text = "No hay palabra del día disponible."
                binding.txtReflexion.text = ""
                binding.txtFecha.text = ""
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                binding.txtVersiculo.text = msg
                binding.txtReflexion.text = ""
            }
        }

        viewModel.obtenerPorFecha(viewModel.hoy)
    }

    // ---------------------------
    // MULTIMEDIA
    // ---------------------------
    private fun mostrarMultimedia(url: String) {

        binding.imgPalabra.visibility = View.GONE
        binding.imgPlay.visibility = View.GONE
        binding.youtubePlayerView.visibility = View.GONE
        binding.videoPalabra.visibility = View.GONE
        binding.playerPodcast.visibility = View.GONE

        when {
            url.contains("youtube") || url.contains("youtu.be") -> mostrarMiniaturaYoutube(url)
            url.endsWith(".mp3") || url.endsWith(".wav") -> mostrarAudio(url)
            url.endsWith(".mp4") -> mostrarVideo(url)
            url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".jpeg") -> mostrarImagen(url)
        }
    }

    // ---------------------------
    // MINIATURA + PLAY + YOUTUBE EMBEBIDO
    // ---------------------------
    private fun mostrarMiniaturaYoutube(url: String) {
        binding.imgPalabra.visibility = View.VISIBLE
        binding.imgPlay.visibility = View.VISIBLE

        val videoId = when {
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/")
            url.contains("watch?v=") -> url.substringAfter("watch?v=").substringBefore("&")
            else -> ""
        }

        if (videoId.isNotEmpty()) {
            val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

            Glide.with(this)
                .load(thumbnailUrl)
                .into(binding.imgPalabra)

            val clickListener = View.OnClickListener {
                binding.imgPalabra.visibility = View.GONE
                binding.imgPlay.visibility = View.GONE
                binding.youtubePlayerView.visibility = View.VISIBLE

                lifecycle.addObserver(binding.youtubePlayerView)

                binding.youtubePlayerView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })
            }

            binding.imgPalabra.setOnClickListener(clickListener)
            binding.imgPlay.setOnClickListener(clickListener)
        }
    }

    // ---------------------------
    // IMAGEN
    // ---------------------------
    private fun mostrarImagen(url: String) {
        binding.imgPalabra.visibility = View.VISIBLE
        Glide.with(this).load(url).into(binding.imgPalabra)
    }

    // ---------------------------
    // VIDEO MP4
    // ---------------------------
    private fun mostrarVideo(url: String) {
        binding.videoPalabra.visibility = View.VISIBLE
        binding.videoPalabra.setVideoURI(Uri.parse(url))
        binding.videoPalabra.setOnPreparedListener { it.start() }
    }

    // ---------------------------
    // AUDIO
    // ---------------------------
    private fun mostrarAudio(url: String) {
        binding.playerPodcast.visibility = View.VISIBLE
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerPodcast.player = player
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        player = null
        _binding = null
    }
}
