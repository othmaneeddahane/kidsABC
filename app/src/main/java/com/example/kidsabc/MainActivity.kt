package com.example.kidsabc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Activité principale qui affiche la liste des lettres arabes
 * L'enfant peut cliquer sur une lettre pour aller la tracer
 */
class MainActivity : AppCompatActivity() {

    // RecyclerView pour afficher les lettres en grille
    private lateinit var lettersRecyclerView: RecyclerView
    private lateinit var letterAdapter: LetterAdapter
    private lateinit var arabicButton: Button
    private lateinit var frenchButton: Button
    private var isArabicMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialise les boutons et la RecyclerView
        setupButtons()
        setupRecyclerView()
    }

    /**
     * Configure les boutons de sélection d'alphabet
     */
    private fun setupButtons() {
        arabicButton = findViewById(R.id.arabicButton)
        frenchButton = findViewById(R.id.frenchButton)
        
        arabicButton.setOnClickListener {
            switchToArabic()
        }
        
        frenchButton.setOnClickListener {
            switchToFrench()
        }
        
        updateButtonStyles()
    }
    
    /**
     * Configure la RecyclerView avec les lettres
     */
    private fun setupRecyclerView() {
        lettersRecyclerView = findViewById(R.id.lettersRecyclerView)
        
        // Crée l'adaptateur avec action de clic
        letterAdapter = LetterAdapter(getCurrentAlphabet()) { letter ->
            openDrawingActivity(letter)
        }
        
        // Configure la RecyclerView en grille (3 colonnes)
        lettersRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = letterAdapter
        }
    }
    
    /**
     * Bascule vers l'alphabet arabe
     */
    private fun switchToArabic() {
        isArabicMode = true
        updateButtonStyles()
        updateRecyclerView()
    }
    
    /**
     * Bascule vers l'alphabet français
     */
    private fun switchToFrench() {
        isArabicMode = false
        updateButtonStyles()
        updateRecyclerView()
    }
    
    /**
     * Met à jour les styles des boutons
     */
    private fun updateButtonStyles() {
        if (isArabicMode) {
            arabicButton.setBackgroundResource(R.drawable.button_selected)
            frenchButton.setBackgroundResource(R.drawable.button_unselected)
        } else {
            arabicButton.setBackgroundResource(R.drawable.button_unselected)
            frenchButton.setBackgroundResource(R.drawable.button_selected)
        }
    }
    
    /**
     * Met à jour la RecyclerView avec le bon alphabet
     */
    private fun updateRecyclerView() {
        letterAdapter = LetterAdapter(getCurrentAlphabet()) { letter ->
            openDrawingActivity(letter)
        }
        lettersRecyclerView.adapter = letterAdapter
    }
    
    /**
     * Retourne l'alphabet actuel
     */
    private fun getCurrentAlphabet(): List<ArabicLetter> {
        return if (isArabicMode) ArabicAlphabet.letters else FrenchAlphabet.letters
    }

    /**
     * Ouvre l'activité de tracé pour une lettre
     */
    private fun openDrawingActivity(letter: ArabicLetter) {
        val intent = Intent(this, DrawingActivity::class.java).apply {
            putExtra("LETTER_CHAR", letter.character)
            putExtra("LETTER_NAME", letter.name)
        }
        startActivity(intent)
    }
}