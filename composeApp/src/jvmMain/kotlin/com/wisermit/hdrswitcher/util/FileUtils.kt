package com.wisermit.hdrswitcher.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.image.BufferedImage
import java.awt.image.MultiResolutionImage
import java.io.File
import javax.swing.ImageIcon
import javax.swing.filechooser.FileSystemView

object FileUtils {

    fun getIcon(file: File): ImageBitmap? {
        val icon = FileSystemView.getFileSystemView().getSystemIcon(file)
        val imageIcon = icon as? ImageIcon
        val multiResolutionImage = imageIcon?.image as? MultiResolutionImage
        val bufferedImage = multiResolutionImage?.resolutionVariants
            ?.filterIsInstance<BufferedImage>()
            ?.maxBy { it.width }
        return bufferedImage?.toComposeImageBitmap()
    }
}