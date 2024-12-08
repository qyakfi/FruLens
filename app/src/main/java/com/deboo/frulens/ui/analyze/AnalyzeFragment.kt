package com.deboo.frulens.ui.analyze

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.deboo.frulens.databinding.FragmentAnalyzeBinding
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import com.deboo.frulens.utils.ImageClassifierHelper

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private val viewModel: AnalyzeViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val analyzeViewModel = ViewModelProvider(this).get(AnalyzeViewModel::class.java)

        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        analyzeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        analyzeViewModel.currentImageUri?.let { uri ->
            showImage(uri)
        }

        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = this
        )

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
        binding.cameraButton.setOnClickListener { analyzeImage() }
        return root
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            showImage(viewModel.currentImageUri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage(uri: Uri?) {
        uri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        viewModel.currentImageUri?.let { uri ->
            // Show the progress bar
            binding.progressIndicator.visibility = View.VISIBLE

            // Launch a coroutine to perform classification
            lifecycleScope.launch {
                imageClassifierHelper.classifyStaticImage(uri)
            }
        } ?: run {
            showToast("Please select an image first.")
        }
    }

    fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        binding.progressIndicator.visibility = View.GONE
        results?.let {
            var concLabel = ""
            var highscore = 0f
            val allResults = mutableListOf<String>()

            // Iterate over all classifications and categories to find the highest score
            it.forEach { classification ->
                classification.categories.forEach { category ->
                    allResults.add("Label: ${category.label}, Score: ${category.score}")
                    if (category.score > highscore) {
                        highscore = category.score * 100
                        concLabel = category.label
                    }
                }
            }

            val resultString = ("Conclusion: Identified $concLabel $highscore%" +
                    "\n" +
                    "Inference Time: $inferenceTime ms")

            binding.textDashboard.text = resultString
        }
    }

    fun onError(error: String) {
        showToast(error)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
