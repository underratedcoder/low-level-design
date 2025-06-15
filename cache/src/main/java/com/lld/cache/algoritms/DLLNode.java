package com.lld.cache.algoritms;

import lombok.Getter;

/**
 * Object which is inserted in the {@link DoublyLinkedList}. A single node is expected to be created for each element.
 *
 * @param <E> Type of element to be inserted into the list.
 */
@Getter
public class DLLNode<E> {
    E element;
    DLLNode<E> next;
    DLLNode<E> prev;

    public DLLNode(E element) {
        this.element = element;
        this.next = null;
        this.prev = null;
    }
}
