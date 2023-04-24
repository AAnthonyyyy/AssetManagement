package com.hgm.am.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hgm.am.R

/**
 * @author：  HGM
 * @date：  2023-04-23 15:27
 */
class MyDialogFragment : DialogFragment() {


      override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
      ): View? {
            //进出场动画
            dialog?.window?.setWindowAnimations(R.style.main_menu_animStyle)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_menu, null)

            //拍摄按钮
            val btnPhoto = view.findViewById<TextView>(R.id.tv_take_photo)
            //相册选择按钮
            //val btnSelect = view.findViewById<TextView>(R.id.tv_take_pic)
            //取消按钮
            val btnCancel = view.findViewById<TextView>(R.id.tv_cancel)

            btnCancel.setOnClickListener {
                  dismiss()
            }

            btnPhoto.setOnClickListener {
                  onBtnPhotoClickListener?.onClick(it)
            }

            //btnSelect.setOnClickListener {
            //      onBtnSelectClickListener?.onClick(it)
            //}

            return view
      }

      private var onBtnPhotoClickListener: OnClickListener? = null
      fun setOnBtnPhotoClickListener(listener: OnClickListener) {
            onBtnPhotoClickListener = listener
      }

      private var onBtnSelectClickListener: OnClickListener? = null
      fun setOnBtnSelectClickListener(listener: OnClickListener) {
            onBtnSelectClickListener = listener
      }


      override fun onStart() {
            super.onStart()
            val window = dialog!!.window

            //设置弹窗背景色，否则四周会有间隔空隙
            window?.setBackgroundDrawableResource(R.color.white)
            val attributes = window!!.attributes

            //获取屏幕数据
            val displayMetrics = resources.displayMetrics
            //获取屏幕宽高
            val widthPixels = displayMetrics.widthPixels

            //指定弹窗内容宽高
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT

            //设置在底部显示，默认居中
            attributes.gravity = Gravity.BOTTOM

            //重新设置
            window.attributes = attributes
      }
}