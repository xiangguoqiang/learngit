package com.baidu.tieba.interviewlive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.*;

import com.baidu.adp.lib.guide.Component;
import com.baidu.adp.lib.guide.Guide;
import com.baidu.adp.lib.guide.GuideBuilder;
import com.baidu.adp.lib.util.BdUtilHelper;
import com.baidu.adp.widget.ListView.BdTypeListView;
import com.baidu.tbadk.core.TbadkCoreApplication;
import com.baidu.tbadk.core.util.SkinManager;
import com.baidu.tbadk.core.view.NavigationBar;
import com.baidu.tbadk.editortools.EditorTools;
import com.baidu.tbadk.core.view.TbListCommonPullView;
import com.baidu.tbadk.core.view.TbListViewPullView;
import com.baidu.tieba.R;

public class InterviewLiveView {
	private InterviewLiveActivity mContext;
	private RelativeLayout mRootView;
	private NavigationBar view_navigation_bar;
	private TextView mOriginalPostBtn;
	private BdTypeListView interview_live_list;
	private TbListViewPullView mPullView = null;
	private InterviewLiveAdapterManager mAdapterManager;

	private EditorTools mET;
	private LinearLayout mCommentView = null;
	//private TextView mCommentReplyViewText = null;
	//private TextView mCommentReplyCountViewText;
	private boolean isCommentViewShowing = true;
	private boolean mIsPraised;

	private boolean hasDraft;

	public InterviewLiveView(final InterviewLiveActivity context, View.OnClickListener onClickListener) {
		this.mContext = context;
		mRootView = (RelativeLayout) LayoutInflater.from(context.getPageContext().getPageActivity()).inflate(R.layout.interviewlive_activity, null);
		mContext.setContentView(mRootView);
		initNavigationBar();
		initCommentView();

		mRootView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				resetCommentView();
			}
		});
		interview_live_list = (BdTypeListView) mContext.findViewById(R.id.interview_live_list);
		mPullView = new TbListViewPullView(mContext.getPageContext());
		interview_live_list.setPullRefresh(mPullView);
		mAdapterManager = new InterviewLiveAdapterManager(mContext, interview_live_list);
		mAdapterManager.setCommonOnclickListener(onClickListener);
	}

	private void initNavigationBar() {
		view_navigation_bar = (NavigationBar) mContext.findViewById(R.id.view_navigation_bar);
		view_navigation_bar.addSystemImageButton(NavigationBar.ControlAlign.HORIZONTAL_LEFT, NavigationBar.ControlType.BACK_BUTTON, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.finish();
			}
		});
		view_navigation_bar.setTitleText(R.string.interviewlivetitle);
		mOriginalPostBtn = view_navigation_bar.addTextButton(NavigationBar.ControlAlign.HORIZONTAL_RIGHT, mContext.getResources().getString(R.string.originalpost), mContext.mCommonOnClickListener);
	}

	public TextView getOriginalPostBtn() {
		return mOriginalPostBtn;
	}

	private void initCommentView() {
		mCommentView = (LinearLayout) mRootView.findViewById(R.id.interview_comment_container);

		mCommentView.setVisibility(View.VISIBLE);
//		mCommentReplyViewText.setText(mContext.getResources().getString(R.string.inteview_say));
//
//		mCommentReplyViewText.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// if (mActivity.checkUpIsLogin() == false) {
//				// TiebaStatic.log(new
//				// StatisticItem(PbStatisticKey.PB_LIKE_REGISTER_CLICK).param("obj_locate",
//				// 2).param("fid", mForumId));
//				// return;
//				// }
//
//				// 评论按钮一点击就会弹出工具组件
//				if (mET != null) {
//					initPbEditor();
//				}
//				if (mET != null) {
//
//					isCommentViewShowing = false;
//					// BubbleTipHelper.dealOverdueTipDelay(mActivity, (View)
//					// mET.findToolById(EditorToolsID.TOOL_ID_INPUT).toolView,
//					// false, mBubbleTipRefreshCallback);
//				}
//				if (mCommentView != null) {
//					mCommentView.setVisibility(View.GONE);
//					isCommentViewShowing = false;
//				}
//			}
//		});

	}

	/**
	 * 显示原贴按钮的引导浮层
	 */
	public void showOriginalPostGuide() {
		GuideBuilder builder = new GuideBuilder();
		builder.setTargetView(mOriginalPostBtn).setAlpha(128).setOverlayTarget(false).setOutsideTouchable(false);
		Component head = new Component() {
			@Override
			public View getView(LayoutInflater inflater) {
				View guideView = inflater.inflate(R.layout.original_post_guide, null);
				ImageView guide_tip_arrow = (ImageView) guideView.findViewById(R.id.guide_tip_arrow);
				TextView guide_tip_content = (TextView) guideView.findViewById(R.id.guide_tip_content);
				SkinManager.setBackgroundResource(guide_tip_arrow, R.drawable.pic_tip_arrow);
				SkinManager.setBackgroundResource(guide_tip_content, R.drawable.pic_tip);
				SkinManager.setViewTextColor(guide_tip_content, R.color.cp_cont_i, SkinManager.ViewType.TEXTVIEW);
				return guideView;
			}

			@Override
			public int getAnchor() {
				return Component.ANCHOR_BOTTOM;
			}

			@Override
			public int getFitPosition() {
				return Component.FIT_END;
			}

			@Override
			public int getXOffset() {
				return 0;
			}

			@Override
			public int getYOffset() {
				int y = BdUtilHelper.getDimens(mContext.getActivity(), R.dimen.ds12);
				return -y;
			}
		};
		builder.addComponent(head);
		Guide guide = builder.createGuide();
		guide.setShouldCheckLocInWindow(false);
		guide.show(mContext.getPageContext().getPageActivity());
	}

	public void setEditorTools(EditorTools editorTools) {
		this.mET = editorTools;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		//params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mCommentView.addView(mET, params);
		mET.onChangeSkinType(TbadkCoreApplication.getInst().getSkinType());
		//mET.hide();
	}

	public void initPbEditor() {
		if (mContext == null || mET == null) {
			return;
		}
		mET.display();
	}

	public void setOnScrollListener(AbsListView.OnScrollListener listener) {
		interview_live_list.setOnScrollListener(listener);
	}

	public void resetCommentView() {
		isCommentViewShowing = true;
		mET.hideSoftKey();
		mET.hideTools();
	}

	public void onChangeSkin(int skinType) {
		if (mPullView != null) {
			mPullView.changeSkin(skinType);
		}
		mContext.getLayoutMode().setNightMode(skinType == SkinManager.SKIN_TYPE_NIGHT);
		mContext.getLayoutMode().onModeChanged(mRootView);
		mContext.getLayoutMode().onModeChanged(mCommentView);
		view_navigation_bar.onChangeSkinType(mContext.getPageContext(), skinType);
	}

	public void setData(InterviewLiveData data) {
		if (data != null) {
			mAdapterManager.setData(data.getInterviewDetail());
		}
	}

	public View getRootView() {
		return mRootView;
	}

	public void setListPullRefreshListener(TbListCommonPullView.ListPullRefreshListener lisener) {
		mPullView.setListPullRefreshListener(lisener);
	}

	public void completeRefresh() {
		interview_live_list.completePullRefresh();
	}

	public void setPullRefreshState(boolean canPull) {
		mPullView.setEnable(canPull);
	}

	public void setPrePostWrite() {
		mContext.showProgressBar();
	}

	public void setPostWrite(boolean isOK) {
		mContext.hideProgressBar();
		disMissAllDialog();
	}

	public void disMissAllDialog() {
		// TODO
	}

	public void setHasDraft(boolean hasDraft) {
		this.hasDraft = hasDraft;
	}

	public void hideEditTool() {
		if (mET != null) {
			mET.hide();
		}
	}

	public void showCommonView(boolean needAnimation) {
		if (mCommentView == null) {
			return;
		}
		if (mCommentView != null) {
			if (hasDraft) {
				showDraftTip(needAnimation);
			} else {
				hideDraftTip(needAnimation);
			}

		}
	}

	public void showDraftTip(boolean isNeedAnimation) {
//		if (mCommentView == null || mCommentReplyViewText == null || mCommentReplyCountViewText == null) {
//			return;
//		}
		//mCommentReplyViewText.setText("[草稿待发送]");
//		mCommentReplyCountViewText.setVisibility(View.GONE);
		if (isNeedAnimation) {
			AlphaAnimation animation = new AlphaAnimation(0, 1);
			animation.setDuration(400);
			mCommentView.startAnimation(animation);
		}

		mCommentView.setVisibility(View.VISIBLE);
	}

	public void hideDraftTip(boolean isNeedAnimation) {
//		if (mCommentView == null || mCommentReplyViewText == null || mCommentReplyCountViewText == null) {
//			return;
//		}
		//mCommentReplyViewText.setText("回复楼主");
//		mCommentReplyCountViewText.setVisibility(View.VISIBLE);
		if (isNeedAnimation) {
			AlphaAnimation animation = new AlphaAnimation(0, 1);
			animation.setDuration(400);
			mCommentView.startAnimation(animation);
		}

		mCommentView.setVisibility(View.VISIBLE);
	}

	public void showToast(String data) {
		mContext.showToast(data);
	}

	public int getRichTextId() {
		return R.id.interview_content;
	}

	public BdTypeListView getListView() {
		return interview_live_list;
	}
}
