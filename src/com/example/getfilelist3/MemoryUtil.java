package com.example.getfilelist3;

import java.io.File;
import java.util.ArrayList;

import com.example.getfilelist3.MemoryUtil.MemoryInfo;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;

public class MemoryUtil {
	public class Constants{
		public static final String Audio="content://media/external/audio/media";
		public static final String Video="content://media/external/video/media";
		public static final String Images="content://media/external/images/media";
	}
	
	Context mContext;

	public MemoryUtil(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	/*获得SD卡总大小*/
	public long getSDTotalSize(){
		File file=Environment.getExternalStorageDirectory();
		StatFs stat=new StatFs(file.getPath());					//查询文件系统相关的信息
		long blockSize=stat.getBlockSizeLong();
		long totalBlocks=stat.getBlockCountLong();
		return blockSize*totalBlocks;
	}
	/*获得SD卡剩余容量，即可用大小*/
	public long getSDAvailableSize(){
		File file=Environment.getExternalStorageDirectory();
		StatFs stat=new StatFs(file.getPath());
		long blockSize=stat.getBlockSizeLong();
		long availableBlocks=stat.getAvailableBlocksLong();
		return blockSize*availableBlocks;
	}
	/*获得机身内存总大小*/
	public long getRomTotalSize(){
		File file=Environment.getDataDirectory();
		StatFs stat=new StatFs(file.getPath());
		long blockSize=stat.getBlockSizeLong();
		long totalBlocks=stat.getBlockCountLong();
		return blockSize*totalBlocks;
	}
	/*获得机身可用内存*/
	public long getRomAvailableSize(){
		File file=Environment.getDataDirectory();
		StatFs stat=new StatFs(file.getPath());
		long blockSize=stat.getBlockSizeLong();
		long availableBlocks=stat.getAvailableBlocksLong();
		return blockSize*availableBlocks;
	}
	/*外部存储中所有音频/图片/视频文件所占空间*/
	public long getAMediaTotalSize(String s){
		ArrayList<MemoryInfo> resultList=queryAllMediaList(s);
		//ArrayList<MemoryInfo> resultList=queryAllMediaList(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
		long size=0L;
		if(resultList==null) return size;
		for(MemoryInfo cInfo:resultList){
			File file=new File(cInfo.getFilePath());
			if(file!=null&&file.exists())
				size+=cInfo.getFileSize();
		}
		return size;
	}

	/*外部存储中除音频,图片,视频之外其他文件所占空间*/
	public long getOtherTotalSize(){
		long size=getSDTotalSize()-getSDAvailableSize()
				-getAMediaTotalSize(Constants.Audio)-getAMediaTotalSize(Constants.Images)-getAMediaTotalSize(Constants.Video);
		if(size<0L)
			return 0L;
		return size;
	}
	
	private ArrayList<MemoryInfo> queryAllMediaList(Uri uri) {
		//  //我们只需要两个字段：大小、文件路径					//要返回的数据字段
		Cursor mCursor=mContext.getContentResolver().query(uri, new String[]{MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.DATA}, null, null, null);
		
		ArrayList<MemoryInfo> musicList=new ArrayList<MemoryUtil.MemoryInfo>();
		try {
			if(mCursor.moveToFirst()){
				do{
					MemoryInfo memoryInfo=new MemoryInfo();
					memoryInfo.setFileSize(mCursor.getLong(mCursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
					memoryInfo.setFilePath(mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
					musicList.add(memoryInfo);
				}while(mCursor.moveToNext());
			}
		} finally {
			if(mCursor!=null)
				mCursor.close();
		}
		return musicList;
	}

	private ArrayList<MemoryInfo> queryAllMediaList(String s) {
		//  //我们只需要两个字段：大小、文件路径
		Uri uri=Uri.parse(s);
		//Uri uri=ContentUris.parseId(s);
		Cursor mCursor=mContext.getContentResolver().query(uri, new String[]{MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.DATA}, null, null, null);
		if(mCursor==null){
			Log.e("mCurso", "空，可能是未获得SD卡读写权限");
			return null;
		}
		ArrayList<MemoryInfo> musicList=new ArrayList<MemoryUtil.MemoryInfo>();
		try {
			if(mCursor.moveToFirst()){
				do{
					MemoryInfo memoryInfo=new MemoryInfo();
					memoryInfo.setFileSize(mCursor.getLong(mCursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
					memoryInfo.setFilePath(mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
					musicList.add(memoryInfo);
				}while(mCursor.moveToNext());
			}
		} finally {
			if(mCursor!=null)
				mCursor.close();
		}
		return musicList;
	}


	public class MemoryInfo {
		private long fileSize=0L;
		private String filePath="";
		
		public long getFileSize() {
			return fileSize;
		}
		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		
	}
}
