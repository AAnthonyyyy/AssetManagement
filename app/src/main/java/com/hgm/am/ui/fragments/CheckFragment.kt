package com.hgm.am.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hgm.am.databinding.FragmentCheckBinding
import com.hgm.am.ui.MainActivity
import com.hgm.am.util.Constant.Companion.MY_TAG
import com.hgm.am.util.State
import com.hgm.am.viewmodel.MainViewModel
import java.io.File


class CheckFragment : Fragment(), View.OnClickListener {

      private var _binding: FragmentCheckBinding? = null
      private val binding get() = _binding!!
      private lateinit var viewModel: MainViewModel
      private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
      private lateinit var camUri: Uri
      private lateinit var file: File

      override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
      ): View {
            _binding = FragmentCheckBinding.inflate(inflater, container, false)
            return binding.root
      }

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            init()

            cameraLauncher =
                  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        if (it.resultCode == RESULT_OK) {
                              binding.ivImage.setImageURI(camUri)
                        }
                  }

            viewModel.assetInfo.observe(viewLifecycleOwner, Observer { response ->
                  when (response) {
                        is State.Error -> {
                              binding.tvAssetNumber.text = "查无此资产编号"
                              hideProgressBar(binding.progressBar)
                        }
                        is State.Loading -> {
                              showProgressBar(binding.progressBar)
                        }
                        is State.Success -> {
                              hideProgressBar(binding.progressBar)
                              //成功更新 ui 信息
                              response.data?.let {
                                    with(it.data) {
                                          binding.apply {
                                                tvAssetNumber.text = "资产编号：$assets_number"
                                                tvName.text = "资产名称：$name"
                                                tvModel.text = "Model：$model"
                                                tvUseStatus.text = "使用状态：$use_status"
                                                tvUsername.text = "用户名：${user?.username}"
                                          }
                                    }
                              }
                        }
                  }
            })

            viewModel.upLoadInfo.observe(viewLifecycleOwner, Observer { response ->
                  when (response) {
                        is State.Error -> {
                              Toast.makeText(activity, response.data?.msg, Toast.LENGTH_SHORT)
                                    .show()
                              hideProgressBar(binding.progressBar2)
                        }
                        is State.Loading -> {
                              showProgressBar(binding.progressBar2)
                        }
                        is State.Success -> {
                              hideProgressBar(binding.progressBar2)
                              Toast.makeText(activity, "上传成功", Toast.LENGTH_SHORT).show()
                        }
                  }
            })
      }

      /**
       * 初始化
       */
      private fun init() {
            viewModel = (activity as MainActivity).viewModel

            binding.apply {
                  btnQuery.setOnClickListener(this@CheckFragment)
                  ivImage.setOnClickListener(this@CheckFragment)
                  btnUpLoad.setOnClickListener(this@CheckFragment)
            }
      }

      override fun onClick(view: View?) {
            when (view) {
                  binding.btnQuery -> {
                        if (binding.etAsset.text.toString().isEmpty()) {
                              Toast.makeText(activity, "编号不能为空", Toast.LENGTH_SHORT).show()
                              return
                        }
                        viewModel.getAssetData(binding.etAsset.text.toString())
                  }
                  binding.ivImage -> {
                        openBottomDialog()
                  }
                  binding.btnUpLoad -> {
                        upLoadImg()
                  }
            }
      }

      /**
       * 上传图片到后台
       */
      private fun upLoadImg() {
            if (binding.etAsset.text.toString().isEmpty()) {
                  Toast.makeText(activity, "编号不能为空", Toast.LENGTH_SHORT).show()
                  return
            }
            viewModel.upLoadImg(binding.etAsset.text.toString(), file)
      }

      /**
       * 弹出底部弹窗
       */
      private fun openBottomDialog() {
            val dialog = MyDialogFragment()
            dialog.show(childFragmentManager, null)

            //相机按钮的点击事件
            dialog.setOnBtnPhotoClickListener {
                  openCamera()
                  dialog.dismiss()
            }

            //相册按钮的点击事件
            dialog.setOnBtnSelectClickListener {
                  openAlbum()
                  dialog.dismiss()
            }
      }

      /**
       * 调用相册
       */
      private fun openAlbum() {

      }

      /**
       * 调用相机
       */
      private fun openCamera() {
            val values = ContentValues().apply {
                  put(MediaStore.Images.Media.TITLE, "New Picture")
                  put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            }
            camUri = requireContext().contentResolver.insert(
                  MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                  values
            )!!
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                  putExtra(MediaStore.EXTRA_OUTPUT, camUri)
            }
            cameraLauncher.launch(cameraIntent)

            //把 uri 转换成 file 上传后台
            file = uriToFile(camUri)
            Log.e(MY_TAG, "openCamera: $file")
      }

      /**
       * uri转换图片
       */
      private fun uriToFile(uri: Uri): File {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = activity?.managedQuery(uri, proj, null, null, null)
            val actualImageColumnIndex: Int =
                  cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)!!
            cursor.moveToFirst()
            val imgPath: String = cursor.getString(actualImageColumnIndex)
            return File(imgPath)
      }


      /**
       * 显示进度
       */
      private fun showProgressBar(progressBar: ProgressBar) {
            progressBar.visibility = View.VISIBLE
      }

      /**
       * 隐藏进度
       */
      private fun hideProgressBar(progressBar: ProgressBar) {
            progressBar.visibility = View.GONE
      }

      override fun onDestroy() {
            super.onDestroy()
            _binding = null
      }
}