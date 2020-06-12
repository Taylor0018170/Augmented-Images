package com.augmentedimages.app.presentation.list

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.augmentedimages.app.R
import com.augmentedimages.app.core.ext.getListImage
import com.augmentedimages.app.core.ext.renameTo
import com.augmentedimages.app.data.model.MarkerModel
import com.augmentedimages.app.presentation.list.epoxy.ListMarkersController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.ar.core.Session
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_list_markers.*
import java.io.File

class ListMarkersFragment : Fragment(), View.OnClickListener,
    ListMarkersController.ListMarkersListener {

    private val listMarkersController: ListMarkersController by lazy { ListMarkersController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list_markers, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (!report.isAnyPermissionPermanentlyDenied) {
                        fabAddImage.setOnClickListener(this@ListMarkersFragment)
                        initRecycler()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {

                }
            }).check()
    }

    private fun initRecycler() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        rvMarkers.apply {
            layoutManager = linearLayoutManager
            adapter = listMarkersController.adapter
        }

        listMarkersController.setListener(this)
        listMarkersController.setItems(requireContext().filesDir.getListImage())
    }

    override fun onClick(v: View?) {
        when (v) {
            fabAddImage -> {
                ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                showCreateDialog(data)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun itemEdit(position: Int, item: MarkerModel) {
        MaterialDialog(requireContext()).show {
            title(R.string.enter_name)
            input(waitForPositiveButton = false) { _, _ -> }

            positiveButton(text = "Submit", click = {
                if (File(requireContext().filesDir, "${getInputField().text}.jpg").exists()) {
                    getInputField().error = "File with this name is exist"
                } else {
                    val model =
                        requireContext().renameTo(
                            File(item.markerPath),
                            "${getInputField().text}.jpg"
                        )
                    listMarkersController.updateList(position, model)
                }
            })
        }
    }

    private fun showCreateDialog(data: Intent?) {
        MaterialDialog(requireContext()).show {
            title(R.string.enter_name)
            input(waitForPositiveButton = false) { _, _ -> }

            positiveButton(text = "Submit", click = {
                val file: File? = ImagePicker.getFile(data)
                val path = requireContext().filesDir
                if (File(path, "${getInputField().text}.jpg").exists()) {
                    getInputField().error = "File with this name is exist"
                } else {
                    File(path, "${getInputField().text}.jpg").apply {
                        file?.copyTo(this, true)
                        listMarkersController.updateList(
                            MarkerModel(
                                System.currentTimeMillis(),
                                absolutePath,
                                name
                            )
                        )
                    }
                    dismiss()
                }
            })
            noAutoDismiss()
        }
    }

    override fun itemRemoved(position: Int, item: MarkerModel) {
        File(item.markerPath).delete()
        listMarkersController.updateList(position)
    }
}
