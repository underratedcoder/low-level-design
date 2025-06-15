package com.lld.cache.algoritms;

import com.lld.cache.algoritms.exceptions.InvalidElementException;
import lombok.Getter;

import java.util.NoSuchElementException;

/**
 * An object which support creating a list with non-contiguous memory allocation. You cannot access a random element
 * directly using index. But if you have a pointer to a node, then you can traverse the list both in forward and
 * backward direction in the list.
 *
 * @param <E> Type of element stored in list.
 */
public class DoublyLinkedList<E> {

    DLLNode<E> dummyHead;
    DLLNode<E> dummyTail;

    public DoublyLinkedList() {
        // We can instantiate these by null, since we are never gonna use val for these dummyNodes
        dummyHead = new DLLNode<>(null);
        dummyTail = new DLLNode<>(null);

        // Also Initially there are no items
        // so just join dummyHead and Tail, we can add items in between them easily.
        dummyHead.next = dummyTail;
        dummyTail.prev = dummyHead;
    }

    /**
     * Method to detach a random node from the doubly linked list. The node itself will not be removed from the memory.
     * Just that it will be removed from the list and becomes orphaned.
     *
     * @param node Node to be detached.
     */
    public void detachNode(DLLNode<E> node) {
        // Just Simply modifying the pointers.
        if (node != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    /**
     * Helper method to add a node at the end of the list.
     *
     * @param node Node to be added.
     */
    public void addNodeAtLast(DLLNode<E> node) {
        DLLNode tailPrev = dummyTail.prev;
        tailPrev.next = node;
        node.next = dummyTail;
        dummyTail.prev = node;
        node.prev = tailPrev;
    }

    /**
     * Helper method to add an element at the end.
     *
     * @param element Element to be added.
     * @return Reference to new node created for the element.
     */
    public DLLNode<E> addElementAtLast(E element) {
        if (element == null) {
            throw new InvalidElementException();
        }
        DLLNode<E> newNode = new DLLNode<>(element);
        addNodeAtLast(newNode);
        return newNode;
    }

    public boolean isItemPresent() {
        return dummyHead.next != dummyTail;
    }

    public DLLNode getFirstNode() throws NoSuchElementException {
        DLLNode item = null;
        if (!isItemPresent()) {
            return null;
        }
        return dummyHead.next;
    }

    public DLLNode getLastNode() throws NoSuchElementException {
        DLLNode item = null;
        if (!isItemPresent()) {
            return null;
        }
        return dummyTail.prev;
    }
}
