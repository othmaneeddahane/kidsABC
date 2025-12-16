package com.example.kidsabc

import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * Activité pour tracer une lettre arabe
 */
class DrawingActivity : AppCompatActivity() {

    // Vues de l'interface
    private lateinit var letterDisplay: TextView
    private lateinit var letterNameDisplay: TextView
    private lateinit var drawingView: DrawingView   // Zone de dessin
    private lateinit var clearButton: LinearLayout  // Bouton pour effacer
    private lateinit var playButton: LinearLayout   // Bouton de son
    private var mediaPlayer: MediaPlayer? = null    // Lecteur audio
    private var textToSpeech: TextToSpeech? = null  // Synthèse vocale


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)
        
        // Initialise les vues
        setupViews()
        
        // Récupère les données de la lettre depuis l'activité précédente
        val letterChar = intent.getStringExtra("LETTER_CHAR") ?: "ا"
        val letterName = intent.getStringExtra("LETTER_NAME") ?: "Alif"
        
        // Affiche les informations de la lettre
        displayLetter(letterChar, letterName)
        
        // Configure les boutons
        setupButtons()
        
        // Initialise Text-to-Speech
        setupTextToSpeech()
    }

    /**
     * Initialise toutes les vues de l'interface
     */
    private fun setupViews() {
        letterDisplay = findViewById(R.id.letterDisplay)
        letterNameDisplay = findViewById(R.id.letterNameDisplay)
        drawingView = findViewById(R.id.drawingView)
        clearButton = findViewById(R.id.clearButton)
        playButton = findViewById(R.id.playButton)
    }

    /**
     * Affiche la lettre et son nom
     */
    private fun displayLetter(character: String, name: String) {
        letterDisplay.text = character
        letterNameDisplay.text = name
    }

    /**
     * Configure les actions des boutons
     */
    private fun setupButtons() {
        // Bouton pour effacer le dessin
        clearButton.setOnClickListener {
            drawingView.clearDrawing()
        }
        
        // Bouton pour jouer le son de la lettre
        playButton.setOnClickListener {
            playLetterSound()
        }
    }

    /**
     * Joue le son de la lettre arabe
     */
    private fun playLetterSound() {
        val letterChar = intent.getStringExtra("LETTER_CHAR") ?: "ا"
        textToSpeech?.speak(letterChar, TextToSpeech.QUEUE_FLUSH, null, null)
    }
    
    /**
     * Initialise Text-to-Speech en arabe
     */
    private fun setupTextToSpeech() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val letterChar = intent.getStringExtra("LETTER_CHAR") ?: "ا"
                val language = if (letterChar.matches(Regex("[a-zA-Z]")))
                    Locale.FRENCH else Locale("ar")
                textToSpeech?.language = language
            }
        }
    }
    
    /**
     * Retourne la ressource audio pour une lettre
     */
    private fun getSoundResource(letter: String): Int {
        return when (letter) {
            "ا" -> R.raw.alif
            "ب" -> R.raw.ba
            "ت" -> R.raw.ta
            "ث" -> R.raw.tha
            "ج" -> R.raw.jim
            "ح" -> R.raw.ha
            "خ" -> R.raw.kha
            "د" -> R.raw.dal
            "ذ" -> R.raw.dhal
            "ر" -> R.raw.ra
            "ز" -> R.raw.zay
            "س" -> R.raw.sin
            "ش" -> R.raw.shin
            "ص" -> R.raw.sad
            "ض" -> R.raw.dad
            "ط" -> R.raw.ta2
            "ظ" -> R.raw.dha
            "ع" -> R.raw.ayn
            "غ" -> R.raw.ghayn
            "ف" -> R.raw.fa
            "ق" -> R.raw.qaf
            "ك" -> R.raw.kaf
            "ل" -> R.raw.lam
            "م" -> R.raw.mim
            "ن" -> R.raw.nun
            "ه" -> R.raw.ha2
            "و" -> R.raw.waw
            "ي" -> R.raw.ya
            else -> 0
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        textToSpeech?.shutdown()
    }


}