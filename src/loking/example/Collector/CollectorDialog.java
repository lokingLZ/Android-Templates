package loking.example.Collector;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class CollectorDialog {
   Context context;
   AlertDialog.Builder mbuilder;
   ProgressDialog mprocessDialog;
   DatePickerDialog mdatePickerDialog;
   TimePickerDialog timePickerDialog;

   public CollectorDialog(Context context)
   {
       this.context = context;
       this.mbuilder = new AlertDialog.Builder(context);
   }

   //	普通的对话框
   public void showNormalDialog()
   {
       mbuilder.setTitle("提示");
       mbuilder.setMessage("确认退出吗？");
       mbuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

           @Override
           public void onClick(DialogInterface dialog, int which)
           {
               /**
                * 具体事件
                */
               dialog.dismiss();
           }
       });

       mbuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

           @Override
           public void onClick(DialogInterface dialog, int which)
           {
               /**
                * 具体事件
                */
               dialog.dismiss();
           }
       });

       mbuilder.create().show();

//		new AlertDialog.Builder(context).setTitle("提示").setMessage("确认退出吗？").setPositiveButton("确认", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				mainActivity.finish();
//			}
//		}).setNegativeButton("取消", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		}).create().show();

   }

   //	列表对话框
   public void showListDialog(String[] items)
   {
       mbuilder.setTitle("列表框");
       mbuilder.setItems(items, null);
       mbuilder.setNegativeButton("确定", null);
       mbuilder.create().show();

   }

   //	单选对话框
   public void showRadioDialog(String[] items)
   {
       mbuilder.setTitle("单选框");
       mbuilder.setIcon(android.R.drawable.ic_dialog_info);
       mbuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

           @Override
           public void onClick(DialogInterface dialog, int which)
           {
               /**
                * 具体事件
                */
               dialog.dismiss();
           }
       });

       mbuilder.setPositiveButton("确认", null);
       mbuilder.setNegativeButton("取消", null);
       mbuilder.create().show();
   }

   //	复选对话框
   public void showCheckboxDialog(String[] items)
   {
       mbuilder.setTitle("复选框");
       mbuilder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {

           @Override
           public void onClick(DialogInterface dialog, int which, boolean isChecked)
           {
               /**
                * 具体事件
                */
               dialog.dismiss();
           }
       });

       mbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

           @Override
           public void onClick(DialogInterface dialog, int which)
           {
               /**
                * 具体事件
                */
               dialog.dismiss();
           }
       });

       mbuilder.setNegativeButton("取消", null);
       mbuilder.create().show();
   }

   //	读取中的对话框
   public void showProgressDialog()
   {
       mprocessDialog= new ProgressDialog(context);
       mprocessDialog.setTitle("进度条框");
       mprocessDialog.setMessage("内容读取中...");
       mprocessDialog.setIndeterminate(true);
       mprocessDialog.setCancelable(true);
       mprocessDialog.show();
   }

   //	日期对话框
   public void showDatePickerDialog()
   {
       mdatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){

           @Override
           public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
           {
               /**
                * 具体事件
                */
           }
       }, 2000, 0, 1);

       mdatePickerDialog.show();
   }

   //	时间对话框
   public void showTimePickerDialog()
   {
       timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener()
       {
           @Override
           public void onTimeSet(TimePicker view, int hourOfDay, int minute)
           {
               /**
                * 具体事件
                */
           }
       }, 12, 0, true);

       timePickerDialog.show();
   }
}

