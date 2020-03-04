package com.shanshuan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzifeng on 2020/3/3.
 */
public class Test {
    public static void main(String[] args) {


        List<MenuItem> list = init();
        List<MenuItem> result=new ArrayList<>();
        for (MenuItem menuItem:getRoot(list)){
            menuItem=bulidTree(menuItem,list);
            result.add(menuItem);
        }
        System.out.println(result);
    }

    /**
     * 构建菜单的list   build  menu's list
     * @param menuItem  需要构建的菜单   need  bulid   menu
     * @param all  所有的菜单数据   all menus
     * @return  构建完的 菜单结构  build  finished  menu
     */
    private static MenuItem bulidTree(MenuItem menuItem, List<MenuItem> all){
        List<MenuItem> result=new ArrayList<>();
        all.stream().forEach(a->{
            if(a.getFid()==menuItem.getId()){
                a=bulidTree(a,all);
                result.add(a);
            }
        });
        if(result!=null&&result.size()>0){
            menuItem.setList(result);
        }
       return menuItem;
    }

    /**
     * 获取根菜单  get root menus
     * @param list
     * @return
     */
    private static List<MenuItem> getRoot(List<MenuItem> list) {
        List<MenuItem> root=new ArrayList<MenuItem>();
        list.stream().forEach(a->{
            if(a.getFid()==0){
                root.add(a);
            }
        });
        return  root;
    }

    /**
     * 初始化数据
     * @return
     */
    private static List<MenuItem> init() {
        List<MenuItem>  list=new ArrayList<MenuItem>();
        MenuItem one=new MenuItem("一级1",1,0,null);
        MenuItem two=new MenuItem("一级2",2,0,null);
        MenuItem three=new MenuItem("一级3",3,0,null);
        MenuItem four=new MenuItem("二级11",4,1,null);
        MenuItem five=new MenuItem("二级12",5,1,null);
        MenuItem six=new MenuItem("二级13",6,1,null);
        MenuItem seven=new MenuItem("二级21",7,2,null);
        MenuItem eight=new MenuItem("二级22",8,2,null);
        MenuItem nine=new MenuItem("二级23",9,2,null);
        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);
        list.add(five);
        list.add(six);
        list.add(seven);
        list.add(eight);
        list.add(nine);
        return  list;
    }
}
