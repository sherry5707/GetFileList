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
	
	/*���SD���ܴ�С*/
	public long getSDTotalSize(){
		File file=Environment.getExternalStorageDirectory();
		StatFs stat=new StatFs(file.getPath());					//��ѯ�ļ�ϵͳ��ص���Ϣ
		long blockSize=stat.getBlockSizeLong();
		long totalBlocks=stat.getBlockCountLong();
		return blockSize*totalBlocks;
	}
	/*���SD��ʣ�������������ô�С*/
	public long getSDAvailableSize(){
		File file=Environment.getExternalStorageDirectory();
		StatFs stat=new StatFs(file.getPath());
		long blockSize=stat.getBlockSizeLong();
		long availableBlocks=stat.getAvailableBlocksLong();
		return blockSize*availableBlocks;
	}
	/*��û����ڴ��ܴ�С*/
	public long getRomTotalSize(){
		File file=Environment.getDataDirectory();
		StatFs stat=new StatFs(file.getPath());
		long blockSize=stat.getBlockSizeLong();
		long totalBlocks=stat.getBlockCountLong();
		return blockSize*totalBlocks;
	}
	/*��û�������ڴ�*/
	public long getRomAvailableSize(){
		File file=Environment.getDataDirectory();
		StatFs stat=new StatFs(file.getPath());
		long blockSize=stat.getBlockSizeLong();
		long availableBlocks=stat.getAvailableBlocksLong();
		return blockSize*availableBlocks;
	}
	/*�ⲿ�洢��������Ƶ/ͼƬ/��Ƶ�ļ���ռ�ռ�*/
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

	/*�ⲿ�洢�г���Ƶ,ͼƬ,��Ƶ֮�������ļ���ռ�ռ�*/
	public long getOtherTotalSize(){
		long size=getSDTotalSize()-getSDAvailableSize()
				-getAMediaTotalSize(Constants.Audio)-getAMediaTotalSize(Constants.Images)-getAMediaTotalSize(Constants.Video);
		if(size<0L)
			return 0L;
		return size;
	}
	
	private ArrayList<MemoryInfo> queryAllMediaList(Uri uri) {
		//  //����ֻ��Ҫ�����ֶΣ���С���ļ�·��					//Ҫ���ص������ֶ�
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
		//  //����ֻ��Ҫ�����ֶΣ���С���ļ�·��
		Uri uri=Uri.parse(s);
		//Uri uri=ContentUris.parseId(s);
		Cursor mCursor=mContext.getContentResolver().query(uri, new String[]{MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.DATA}, null, null, null);
		if(mCursor==null){
			Log.e("mCurso", "�գ�������δ���SD����дȨ��");
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
