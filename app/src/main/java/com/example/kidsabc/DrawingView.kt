package com.example.kidsabc

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

/**
 * Vue personnalisée pour dessiner avec le doigt
 * Hérite de View pour créer notre propre composant de dessin
 */
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Chemin que l'enfant dessine avec son doigt
    private var drawPath = Path()
    
    // Pinceau pour dessiner (couleur, épaisseur...)
    private var drawPaint = Paint().apply {
        color = Color.parseColor("#FF6B6B") // Couleur par défaut: rouge vif
        isAntiAlias = true          // Lissage pour un trait plus joli
        strokeWidth = 20f           // Épaisseur du trait (assez gros pour les enfants)
        style = Paint.Style.STROKE  // Style trait (pas remplissage)
        strokeJoin = Paint.Join.ROUND   // Jointures arrondies
        strokeCap = Paint.Cap.ROUND     // Extrémités arrondies
    }
    
    // Bitmap pour sauvegarder le dessin
    private var canvasBitmap: Bitmap? = null
    private var drawCanvas: Canvas? = null
    
    // Mode arc-en-ciel
    var isRainbowMode = false
    private var rainbowColors = listOf(
        Color.parseColor("#FF6B6B"), // Rouge
        Color.parseColor("#FFA500"), // Orange
        Color.parseColor("#FFD93D"), // Jaune
        Color.parseColor("#6BCB77"), // Vert
        Color.parseColor("#4D96FF"), // Bleu
        Color.parseColor("#9B59B6"), // Violet
        Color.parseColor("#FF85B3")  // Rose
    )
    private var currentRainbowIndex = 0
    
    // Mode paillettes
    var isSparkleMode = false
    private val sparklePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    
    // Liste des chemins pour l'annulation
    private val pathList = mutableListOf<PathData>()
    
    // Classe pour stocker les données d'un chemin
    private data class PathData(
        val path: Path,
        val color: Int,
        val strokeWidth: Float
    )

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
        
        // Dessine tous les chemins sauvegardés
        pathList.forEach { pathData ->
            val paint = Paint(drawPaint).apply {
                color = pathData.color
                strokeWidth = pathData.strokeWidth
            }
            canvas.drawPath(pathData.path, paint)
        }
        
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
                
                // Change la couleur en mode arc-en-ciel
                if (isRainbowMode) {
                    currentRainbowIndex = (currentRainbowIndex + 1) % rainbowColors.size
                    drawPaint.color = rainbowColors[currentRainbowIndex]
                }
            }
            
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(touchX, touchY)
                
                // Ajoute des paillettes en mode sparkle
                if (isSparkleMode) {
                    addSparkles(touchX, touchY)
                }
            }
            
            MotionEvent.ACTION_UP -> {
                // Sauvegarde le chemin dans la liste
                if (!drawPath.isEmpty) {
                    val newPath = Path(drawPath)
                    pathList.add(PathData(newPath, drawPaint.color, drawPaint.strokeWidth))
                }
                drawPath.reset()
            }
        }
        
        // Redessine la vue
        invalidate()
        return true
    }
    
    /**
     * Ajoute des paillettes autour du tracé
     */
    private fun addSparkles(x: Float, y: Float) {
        drawCanvas?.let { canvas ->
            repeat(3) {
                val offsetX = x + Random.nextFloat() * 40 - 20
                val offsetY = y + Random.nextFloat() * 40 - 20
                val sparkleSize = Random.nextFloat() * 6 + 2
                
                // Couleur aléatoire brillante
                val sparkleColors = listOf(
                    Color.parseColor("#FFD700"), // Or
                    Color.parseColor("#FFFF00"), // Jaune vif
                    Color.parseColor("#FF69B4"), // Rose vif
                    Color.parseColor("#00FFFF"), // Cyan
                    Color.parseColor("#FFFFFF")  // Blanc
                )
                sparklePaint.color = sparkleColors.random()
                
                canvas.drawCircle(offsetX, offsetY, sparkleSize, sparklePaint)
            }
        }
    }

    /**
     * Efface tout le dessin
     * Appelé quand on appuie sur le bouton "Effacer"
     */
    fun clearDrawing() {
        // Efface tous les chemins
        pathList.clear()
        // Efface le chemin en cours
        drawPath.reset()
        // Redessine la vue
        invalidate()
    }
    
    /**
     * Annule le dernier trait
     */
    fun undoLastStroke() {
        if (pathList.isNotEmpty()) {
            pathList.removeAt(pathList.size - 1)
            invalidate()
        }
    }
    
    /**
     * Change la couleur du pinceau
     */
    fun setDrawColor(color: Int) {
        drawPaint.color = color
        isRainbowMode = false // Désactive le mode arc-en-ciel
    }
    
    /**
     * Change l'épaisseur du pinceau
     */
    fun setBrushSize(size: Float) {
        drawPaint.strokeWidth = size
    }
    
    /**
     * Active/désactive le mode arc-en-ciel
     */
    fun toggleRainbowMode() {
        isRainbowMode = !isRainbowMode
        isSparkleMode = false // Désactive les paillettes
    }
    
    /**
     * Active/désactive le mode paillettes
     */
    fun toggleSparkleMode() {
        isSparkleMode = !isSparkleMode
        isRainbowMode = false // Désactive l'arc-en-ciel
    }
}