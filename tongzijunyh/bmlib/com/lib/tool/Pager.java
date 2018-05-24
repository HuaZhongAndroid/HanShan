package com.lib.tool;


public class Pager {
	public static final String TAG = "Pager";
	private int pageIndex = 0;
	private int pageSize = 10;
	private int lastReturnSize = -1;

	/**
	 * 如果pageSize不等于上次返回的数据量，说明是最后一页
	 * @return
	 */
	public boolean isLastPage(){
		return lastReturnSize!=-1 && lastReturnSize < pageSize;
	}
	
	public Pager(int pageSize) {
		this.pageSize = pageSize;
	}

	public int currentPage(){
		return pageIndex;
	}
	
	/**
	 * 获取要加载的下一页页码
	 * @return
	 */
	public int nextPage(){
		return pageIndex+1;
	}
	/**
	 * 获取要加载的下一页页码(转换成字符串)
	 * @return
	 */
	public String nextPageToStr(){
		return (pageIndex+1)+"";
	}

	/**
	 * 设置起始页
	 */
	public void setFirstPage(){
		this.pageIndex = 0;
		this.lastReturnSize = -1;
	}
	
	/**
	 * 数据返回成功以后，调用此方法
	 * @param pageIndex
	 * @param lastReturnSize
	 */
	public void setCurrentPage(int pageIndex, int lastReturnSize){
		this.pageIndex = pageIndex;
		this.lastReturnSize = lastReturnSize;
	}
}
