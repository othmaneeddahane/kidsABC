package com.example.kidsabc

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
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
    private lateinit var rainbowButton: LinearLayout // Bouton arc-en-ciel
    private lateinit var sparkleButton: LinearLayout // Bouton paillettes
    private lateinit var undoButton: LinearLayout   // Bouton annuler
    private var mediaPlayer: MediaPlayer? = null    // Lecteur audio
    private var textToSpeech: TextToSpeech? = null  // Synthèse vocale
    
    // Palette de couleurs
    private val colorViews = mutableListOf<View>()
    private var currentColorView: View? = null
    
    // Tailles de pinceau
    private val brushViews = mutableListOf<View>()
    private var currentBrushView: View? = null


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
        rainbowButton = findViewById(R.id.rainbowButton)
        sparkleButton = findViewById(R.id.sparkleButton)
        undoButton = findViewById(R.id.undoButton)
        
        // Initialise la palette de couleurs
        setupColorPalette()
        
        // Initialise les tailles de pinceau
        setupBrushSizes()
    }
    
    /**
     * Configure la palette de couleurs interactive
     */
    private fun setupColorPalette() {
        // Récupère toutes les vues de couleur
        colorViews.add(findViewById(R.id.colorRed))
        colorViews.add(findViewById(R.id.colorOrange))
        colorViews.add(findViewById(R.id.colorYellow))
        colorViews.add(findViewById(R.id.colorGreen))
        colorViews.add(findViewById(R.id.colorBlue))
        colorViews.add(findViewById(R.id.colorPurple))
        colorViews.add(findViewById(R.id.colorPink))
        colorViews.add(findViewById(R.id.colorBrown))
        colorViews.add(findViewById(R.id.colorBlack))
        
        // Définit les couleurs correspondantes
        val colors = listOf(
            Color.parseColor("#FF6B6B"), // Rouge
            Color.parseColor("#FFA500"), // Orange
            Color.parseColor("#FFD93D"), // Jaune
            Color.parseColor("#6BCB77"), // Vert
            Color.parseColor("#4D96FF"), // Bleu
            Color.parseColor("#9B59B6"), // Violet
            Color.parseColor("#FF85B3"), // Rose
            Color.parseColor("#8B4513"), // Marron
            Color.parseColor("#2C3E50")  // Noir
        )
        
        // Configure les clics pour chaque couleur
        colorViews.forEachIndexed { index, view ->
            view.setOnClickListener {
                selectColor(view, colors[index])
            }
        }
        
        // Sélectionne la première couleur par défaut
        selectColor(colorViews[0], colors[0])
    }
    
    /**
     * Configure les tailles de pinceau
     */
    private fun setupBrushSizes() {
        brushViews.add(findViewById(R.id.brushSmall))
        brushViews.add(findViewById(R.id.brushMedium))
        brushViews.add(findViewById(R.id.brushLarge))
        
        val brushSizes = listOf(12f, 20f, 35f)
        
        brushViews.forEachIndexed { index, view ->
            view.setOnClickListener {
                selectBrushSize(view, brushSizes[index])
            }
        }
        
        // Sélectionne la taille moyenne par défaut
        selectBrushSize(brushViews[1], brushSizes[1])
    }
    
    /**
     * Sélectionne une taille de pinceau
     */
    private fun selectBrushSize(brushView: View, size: Float) {
        // Retire l'effet de la taille précédente
        currentBrushView?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(200)?.start()
        
        // Applique l'effet à la nouvelle taille
        brushView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start()
        currentBrushView = brushView
        
        // Change la taille du pinceau
        drawingView.setBrushSize(size)
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
            animateButton(it)
        }
        
        // Bouton pour jouer le son de la lettre
        playButton.setOnClickListener {
            playLetterSound()
            animateButton(it)
        }
        
        // Bouton arc-en-ciel
        rainbowButton.setOnClickListener {
            drawingView.toggleRainbowMode()
            animateButton(it)
        }
        
        // Bouton paillettes
        sparkleButton.setOnClickListener {
            drawingView.toggleSparkleMode()
            animateButton(it)
        }
        
        // Bouton annuler
        undoButton.setOnClickListener {
            drawingView.undoLastStroke()
            animateButton(it)
        }
    }
    
    /**
     * Sélectionne une couleur de la palette
     */
    private fun selectColor(colorView: View, color: Int) {
        // Retire l'effet de la couleur précédente
        currentColorView?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(200)?.start()
        
        // Applique l'effet à la nouvelle couleur
        colorView.animate().scaleX(1.3f).scaleY(1.3f).setDuration(200).start()
        currentColorView = colorView
        
        // Change la couleur du pinceau
        drawingView.setDrawColor(color)
    }
    
    /**
     * Anime un bouton quand on clique dessus
     */
    private fun animateButton(view: View) {
        val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f)
        val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f)
        scaleDownX.duration = 100
        scaleDownY.duration = 100
        
        val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f)
        val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f)
        scaleUpX.duration = 100
        scaleUpY.duration = 100
        
        val animatorSet = AnimatorSet()
        animatorSet.play(scaleDownX).with(scaleDownY)
        animatorSet.play(scaleUpX).with(scaleUpY).after(scaleDownX)
        animatorSet.start()
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