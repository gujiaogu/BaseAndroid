package com.yunshu.baseandroid.jpush;

import android.content.Context;
import android.text.TextUtils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yunshu.baseandroid.jpush.TagAliasOperatorHelper.ACTION_SET;
import static com.yunshu.baseandroid.jpush.TagAliasOperatorHelper.sequence;

/**
 * 极光推送设置别名和tag工具类
 *
 * Created by Tyrese on 2017/12/27.
 */

public class TagAliasUtil {

    private Context context;

    public TagAliasUtil(Context context) {
        this.context = context;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 删除别名
     */
    public void deleteAlias() {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_DELETE;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(context.getApplicationContext(), sequence, tagAliasBean);
    }

    /**
     * 删除tags
     */
    public void cleanTags() {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_CLEAN;
        tagAliasBean.isAliasAction = false;
        TagAliasOperatorHelper.getInstance().handleAction(context.getApplicationContext(), sequence, tagAliasBean);
    }

    /**
     * 设置新的别名
     *
     * @param alias 别名
     */
    public void setNewAlias(String alias) {
        onTagAliasAction(ACTION_SET, alias, null);
    }

    /**
     * 设置新的tags
     *
     * @param tags tags
     */
    public void setNewTags(Set<String> tags) {
        onTagAliasAction(ACTION_SET, null, tags);
    }

    /**
     * 设置别名
     * @param action TagAliasOperatorHelper:  ACTION_ADD, ACTION_CHECK, ACTION_CLEAN,
     *               ACTION_DELETE, ACTION_GET, ACTION_SET
     * @param alias 别名
     */
    public void setAlias(int action, String alias) {
        onTagAliasAction(action, alias, null);
    }

    /**
     * 设置tags
     * @param action TagAliasOperatorHelper:  ACTION_ADD, ACTION_CHECK, ACTION_CLEAN,
     *               ACTION_DELETE, ACTION_GET, ACTION_SET
     * @param tags 别名
     */
    public void setTags(int action, Set<String> tags) {
        onTagAliasAction(action, null, tags);
    }

    private void onTagAliasAction(int action, String alias, Set<String> tags) {
        boolean isAliasAction = false;
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        sequence ++;
        if (tags != null) {
            tagAliasBean.tags = tags;
        }
        if (!TextUtils.isEmpty(alias)) {
            isAliasAction = true;
            tagAliasBean.alias = alias;
        }
        tagAliasBean.isAliasAction = isAliasAction;
        TagAliasOperatorHelper.getInstance().handleAction(context.getApplicationContext(), sequence, tagAliasBean);
    }
}
