/*
 * 瀹樼綉鍦扮珯:http://www.mob.com
 * 鎶�鏈敮鎸丵Q: 4006852216
 * 瀹樻柟寰俊:ShareSDK   锛堝鏋滃彂甯冩柊鐗堟湰鐨勮瘽锛屾垜浠皢浼氱涓�鏃堕棿閫氳繃寰俊灏嗙増鏈洿鏂板唴瀹规帹閫佺粰鎮ㄣ�傚鏋滀娇鐢ㄨ繃绋嬩腑鏈変换浣曢棶棰橈紝涔熷彲浠ラ�氳繃寰俊涓庢垜浠彇寰楄仈绯伙紝鎴戜滑灏嗕細鍦�24灏忔椂鍐呯粰浜堝洖澶嶏級
 *
 * Copyright (c) 2013骞� mob.com. All rights reserved.
 */

package com.hewaiming.ALWorkInfo.Share;

import android.content.Intent;
import android.widget.Toast;
import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

/** 寰俊瀹㈡埛绔洖璋僡ctivity绀轰緥 */
public class WXEntryActivity extends WechatHandlerActivity {

	/**
	 * 澶勭悊寰俊鍙戝嚭鐨勫悜绗笁鏂瑰簲鐢ㄨ姹俛pp message
	 * <p>
	 * 鍦ㄥ井淇″鎴风涓殑鑱婂ぉ椤甸潰鏈夆�滄坊鍔犲伐鍏封�濓紝鍙互灏嗘湰搴旂敤鐨勫浘鏍囨坊鍔犲埌鍏朵腑
	 * 姝ゅ悗鐐瑰嚮鍥炬爣锛屼笅闈㈢殑浠ｇ爜浼氳鎵ц銆侱emo浠呬粎鍙槸鎵撳紑鑷繁鑰屽凡锛屼絾浣犲彲
	 * 鍋氱偣鍏朵粬鐨勪簨鎯咃紝鍖呮嫭鏍规湰涓嶆墦寮�浠讳綍椤甸潰
	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null) {
			Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
			startActivity(iLaunchMyself);
		}
	}

	/**
	 * 澶勭悊寰俊鍚戠涓夋柟搴旂敤鍙戣捣鐨勬秷鎭�
	 * <p>
	 * 姝ゅ鐢ㄦ潵鎺ユ敹浠庡井淇″彂閫佽繃鏉ョ殑娑堟伅锛屾瘮鏂硅鏈琩emo鍦╳echatpage閲岄潰鍒嗕韩
	 * 搴旂敤鏃跺彲浠ヤ笉鍒嗕韩搴旂敤鏂囦欢锛岃�屽垎浜竴娈靛簲鐢ㄧ殑鑷畾涔変俊鎭�傛帴鍙楁柟鐨勫井淇�
	 * 瀹㈡埛绔細閫氳繃杩欎釜鏂规硶锛屽皢杩欎釜淇℃伅鍙戦�佸洖鎺ユ敹鏂规墜鏈轰笂鐨勬湰demo涓紝褰撲綔
	 * 鍥炶皟銆�
	 * <p>
	 * 鏈珼emo鍙槸灏嗕俊鎭睍绀哄嚭鏉ワ紝浣嗕綘鍙仛鐐瑰叾浠栫殑浜嬫儏锛岃�屼笉浠呬粎鍙槸Toast
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}

}
