package com.example.kidsabc

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Vue personnalisée pour dessiner avec le doigt
 * Hérite de View pour créer notre propre composant de dessin
 */
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Chemin que l'enfant dessine avec son doigt
    private var drawPath = Path()
    
    // Pinceau pour dessiner (couleur, épaisseur...)
    private var drawPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true          // Lissage pour un trait plus joli
        strokeWidth = 15f           // Épaisseur du trait (assez gros pour les enfants)
        style = Paint.Style.STROKE  // Style trait (pas remplissage)
        strokeJoin = Paint.Join.ROUND   // Jointures arrondies
        strokeCap = Paint.Cap.ROUND     // Extrémités arrondies
    }
    
    // Bitmap pour sauvegarder le dessin
    private var canvasBitmap: Bitmap? = null
    private var drawCanvas: Canvas? = null

    /**
     * On crée notre bitmap de dessin
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Crée un bitmap de la taille de la vue
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
    }

    /**
     * Dessine sur l'écran
     * Appelé automatiquement par Android
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Dessine le bitmap sauvegardé
        canvasBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
        
        // Dessine le chemin en cours
        canvas.drawPath(drawPath, drawPaint)
    }

    /**
     * Gère les touches sur l'écran
     * Appelé quand l'enfant touche, bouge ou lève le doigt
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(touchX, touchY)
            }
            
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(touchX, touchY)
            }
            
            MotionEvent.ACTION_UP -> {
                // Sauvegarde le trait sur le bitmap
                drawCanvas?.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
        }
        
        // Redessine la vue
        invalidate()
        return true
    }

    /**
     * Efface tout le dessin
     * Appelé quand on appuie sur le bouton "Effacer"
     */
    fun clearDrawing() {
        // Efface le bitmap
        canvasBitmap?.eraseColor(Color.TRANSPARENT)
        // Efface le chemin en cours
        drawPath.reset()
        // Redessine la vue
        invalidate()
    }
}