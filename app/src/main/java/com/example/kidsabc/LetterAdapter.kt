package com.example.kidsabc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class LetterAdapter(
    private val letters: List<ArabicLetter>,           // Liste des lettres
    private val onLetterClick: (ArabicLetter) -> Unit  // Action à faire quand on clique sur une lettre
) : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {


    class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val letterText: TextView = itemView.findViewById(R.id.letterText)

    }

    /**
     * Crée une nouvelle vue pour une lettre
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_letter, parent, false)
        return LetterViewHolder(view)
    }

    /**
     * Remplit une vue avec les données d'une lettre
     */

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val letter = letters[position]
        
        // Affiche la lettre arabe en grand
        holder.letterText.text = letter.character
        // Affiche le nom de la lettre

        
        // Définit ce qui se passe quand on clique sur la lettre
        holder.itemView.setOnClickListener {
            onLetterClick(letter)
        }
    }

    /**
     * Retourne le nombre total de lettres
     */

    override fun getItemCount(): Int = letters.size
}