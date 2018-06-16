package com.qfedu.weixinpay.test;

import com.qfedu.weixinpay.utils.PayCommonUtil;

import java.util.LinkedList;

public class Test {
     static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

    }


    public static void main(String[] args) throws Exception {
        String str=PayCommonUtil.weixin_query("76322512");
        System.out.println(str);
        /*Node<Integer> node=new Node<>(null,1,null);
        Node<Integer> top=node;
        for (int i = 2; i <=50; i++) {
            node.next=new Node(node,i,null);
            node=node.next;
        }

        top.prev=node;
        node.next=top;

        //指回头部
        node=top;
        for (int i = 0; i < 52; i++) {
            System.out.println(node.item);


            node=node.prev;
        }
        //小朋友围成圈，且拉上手了。

        node=top;
        for (int i=1;;i++){
            if(node.next==node){
                //他的下一个是他自己
                //一个人了。
                System.out.println(node.item);
                break;
            }

            if(i%3==0){
                //报到3了
                node.prev.next=node.next;
                node.next.prev=node.prev;
            }
            node=node.next;
        }*/
    }
}
