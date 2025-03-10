package com.example.testapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.testapp.databinding.FragmentSecondBinding
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser

class Level0 : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    // Function to read MD files with logging
    private fun readMDfile(fileName: String): String {
        return try {
            val content = requireContext().assets.open("Level0.md").bufferedReader().use { it.readText() }
            Log.d("SecondFragment", "Successfully read $fileName")
            content
        } catch (e: Exception) {
            Log.e("SecondFragment", "Error reading file $fileName: ${e.message}")
            throw e
        }
    }

    // Convert MD to HTML by reading the file internally
    private fun convertToHTML(fileName: String): String {
        var markdownContent = readMDfile(fileName)

        // Replace wiki-style image syntax with standard markdown image syntax
        markdownContent = markdownContent.replace(Regex("!\\[\\[(.+?)]]"), "![$1]($1)")
        Log.d("SecondFragment", "Processed Markdown content: $markdownContent")

        val parser = Parser.builder().build()
        val document = parser.parse(markdownContent)
        val renderer = HtmlRenderer.builder().build()
        return renderer.render(document)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val htmlFragment = convertToHTML("Level0.md")
            val fullHtml = """
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

            binding.webView.loadDataWithBaseURL("file:///android_asset/", fullHtml, "text/html", "utf-8", null)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error loading MD file: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
