package com.example.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testapp.databinding.Level1Binding
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser

class Level1 : Fragment() {
    private var _binding: Level1Binding? = null
    private val binding get() = _binding!!

    private fun readMDfile(fileName: String): String {
        return try {
            val content = requireContext().assets.open("Level1.md").bufferedReader().use { it.readText() }
            Log.d("Level1", "Successfully read $fileName")
            content
    } catch (e: Exception) {
        Log.e("Level1", "Error reading file $fileName: ${e.message}")
        throw e
        }
    }

    private fun convertToHTML(fileName: String): String {
        var markdownContent = readMDfile("level1.md")

        // Replace wiki-style image syntax with standard markdown image syntax
        markdownContent = markdownContent.replace(Regex("!\\[\\[(.+?)]]"), "![$1]($1)")
        Log.d("Level1", "Processed Markdown content: $markdownContent")

        val parser = Parser.builder().build()
        val document = parser.parse(markdownContent)
        val renderer = HtmlRenderer.builder().build()
        return renderer.render(document)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Level1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val htmlFragment = convertToHTML("Level1.md")
            val FullHtml = """
                <html>
              <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                  body { 
                      font-family: sans-serif; 
                      padding: 16px; 
                      color: #000; 
                      background: #fff;
                  }
                  img { 
                      max-width: 100%; 
                      height: auto; 
                      margin: 10px 0;
                  }
                </style>
              </head>
              <body>
                $htmlFragment
              </body>
            </html>
        """.trimIndent()
            binding.webView.loadDataWithBaseURL("file:///android_asset/", FullHtml, "text/html", "utf-8", null)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error loading MD file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_level1_to_levels_menu)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}