/*
 * * Copyright 2015 Swar Shah <swar12894@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cpd4414.assign2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author  Swar Shah <swar12894@gmail.com>
 */
public class OrderQueue {
    Queue<Order> orderQueue = new ArrayDeque<>();
    
    public void add(Order order) {
        if(order.getCustomerId().isEmpty() && order.getCustomerName().isEmpty()){
            throw new NoCustomerException();
        }
        if(order.getListOfPurchases().isEmpty()){
            throw new NoPurchasesException();
        }
        orderQueue.add(order);
        order.setTimeReceived(new Date());
    }
    
    public Order requestNextOrder(){
        if(orderQueue.isEmpty()){
            return null;
        }
        return orderQueue.peek();
    }
    
    public void process(Order order){
            
        if(order.getTimeReceived()!=null){
            boolean inStock = true;
            int count = order.getListOfPurchases().size();
            for (int i = 0; i < count; i++) {
                int pId = order.getListOfPurchases().get(i).getProductId();
                int qtyDB = Inventory.getQuantityForId(pId);
                int qtyReq = order.getListOfPurchases().get(i).getQuantity();
                if(qtyReq >= qtyDB){
                    inStock = false;
                }
            }
            if(inStock){
                order.setTimeProcessed(new Date());
            }            
        }
        else{
            throw new RuntimeException("EXCEPTION: Order does not have time received");
        }
        
    }
    
    public void fulfill(Order order){
        if(order.getTimeProcessed()==null){
            throw new RuntimeException("EXCEPTION: Order Does Not Have Time Processed");
        }
        else if(order.getTimeReceived()==null){
            throw new RuntimeException("EXCEPTION: Order Does Not Have Time Received");
        }
        else{
            boolean inStock = true;
            int count = order.getListOfPurchases().size();
            for (int i = 0; i < count; i++) {
                int pId = order.getListOfPurchases().get(i).getProductId();
                int qtyDB = Inventory.getQuantityForId(pId);
                int qtyReq = order.getListOfPurchases().get(i).getQuantity();
                if(qtyReq >= qtyDB){
                    inStock = false;
                }
            }
            if(inStock){
                order.setTimeFulfilled(new Date());
            }            
        }
    }
    
    public String report(){
        
        if(orderQueue.isEmpty()){
            return "";
        }
        else{
            return "JSON OBJECT";
        }
        
    }
    
    private class NoCustomerException extends RuntimeException{}
    private class NoPurchasesException extends RuntimeException{}
}
