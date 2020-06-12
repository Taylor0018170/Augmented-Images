package com.augmentedimages.app.presentation.ar_scene

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.augmentedimages.app.R
import com.augmentedimages.app.core.ext.getListImage
import com.augmentedimages.app.core.ext.toBitmap
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.test_view.view.*
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

@RequiresApi(Build.VERSION_CODES.N)
class ImageArFragment : ArFragment() {

    private var dataView: CompletableFuture<ViewRenderable>? = null
    private var currentName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.scene.addOnUpdateListener(::onUpdateFrame)
        makeInfoView()
    }

    private fun onUpdateFrame(frameTime: FrameTime) {
        arSceneView.arFrame?.let { frame ->
            val augmentedImages =
                frame.getUpdatedTrackables(
                    AugmentedImage::class.java
                )
            for (augmentedImage in augmentedImages) {
                when (augmentedImage.trackingState) {
                    TrackingState.TRACKING -> {
                        if (currentName != augmentedImage.name) {
                            if (augmentedImage.trackingMethod == AugmentedImage.TrackingMethod.FULL_TRACKING) {
                                currentName = augmentedImage.name
                                onClear()
                                createRenderable(augmentedImage, augmentedImage.name)
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun getSessionConfiguration(session: Session?): Config =
        Config(session).apply {
            planeDiscoveryController.setInstructionView(null)
            updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
            focusMode = Config.FocusMode.AUTO
            session?.configure(this)
            arSceneView.setupSession(session)
            setupAugmentedImagesDb(this, session)
        }

    private fun onClear() {
        val children: List<Node> =
            ArrayList(arSceneView.scene.children)
        for (node in children) {
            if (node is AnchorNode) {
                if (node.anchor != null) {
                    node.anchor?.detach()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createRenderable(
        augmentedImage: AugmentedImage,
        current: String
    ) {
        try {
            val render = dataView?.get()
            val anchorNode =
                AnchorNode(arSceneView.session?.createAnchor(augmentedImage.centerPose))
            arSceneView.scene.removeChild(anchorNode)
            val pose = Pose.makeTranslation(0f, -0.25f, -0.17f)
            Node().apply {
                localPosition = Vector3(pose.tx(), pose.ty(), pose.tz())
                renderable = render
                setParent(anchorNode)
                localRotation = Quaternion(0f, 90f, -90f, 0f)
            }
            arSceneView.scene.addChild(anchorNode)
            render?.let { setNodeData(it, current) }
            makeInfoView()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setNodeData(viewRenderable: ViewRenderable, name: String) {
        with(viewRenderable.view) {
            planetInfoCard.text = name
        }
    }

    private fun makeInfoView() {
        dataView = ViewRenderable.builder()?.setView(requireContext(), R.layout.test_view)?.build()
    }

    private fun setupAugmentedImagesDb(config: Config, session: Session?) {
        AugmentedImageDatabase(session).apply {
            requireContext().filesDir.getListImage().forEach {
                val file = File(it.markerPath)
                addImage(file.name, file.toUri().toBitmap(requireContext()))
            }
            config.augmentedImageDatabase = this
        }
    }
}
