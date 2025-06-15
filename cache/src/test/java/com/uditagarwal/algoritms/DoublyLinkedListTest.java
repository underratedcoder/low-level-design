package com.uditagarwal.algoritms;

import com.google.common.collect.ImmutableList;
import com.lld.cache.algoritms.DoublyLinkedList;
import com.lld.cache.algoritms.DLLNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoublyLinkedListTest {

    @Test
    void testDLLAddition() {
        DLLNode<Integer> node1 = new DLLNode<>(1);
        DLLNode<Integer> node2 = new DLLNode<>(2);
        DLLNode<Integer> node3 = new DLLNode<>(3);
        DLLNode<Integer> node4 = new DLLNode<>(4);

        DoublyLinkedList<Integer> dll = new DoublyLinkedList<>();

        dll.addNodeAtLast(node1);
        verifyDLL(dll, ImmutableList.of(1));

        dll.addNodeAtLast(node2);
        verifyDLL(dll, ImmutableList.of(1, 2));

        dll.addNodeAtLast(node3);
        verifyDLL(dll, ImmutableList.of(1, 2, 3));

        dll.addNodeAtLast(node4);
        verifyDLL(dll, ImmutableList.of(1, 2, 3, 4));

        dll.addElementAtLast(5);
        verifyDLL(dll, ImmutableList.of(1, 2, 3, 4, 5));
    }

    @Test
    void testDLLNodeDetachment() {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<>();

        DLLNode<Integer> node1 = dll.addElementAtLast(1);
        DLLNode<Integer> node2 = dll.addElementAtLast(2);
        DLLNode<Integer> node3 = dll.addElementAtLast(3);
        DLLNode<Integer> node4 = dll.addElementAtLast(4);
        DLLNode<Integer> node5 = dll.addElementAtLast(5);

        verifyDLL(dll, ImmutableList.of(1, 2, 3, 4, 5));

        dll.detachNode(node1);
        verifyDLL(dll, ImmutableList.of(2, 3, 4, 5));

        dll.detachNode(node5);
        verifyDLL(dll, ImmutableList.of(2, 3, 4));

        dll.detachNode(node3);
        verifyDLL(dll, ImmutableList.of(2, 4));

        dll.detachNode(null);
        verifyDLL(dll, ImmutableList.of(2, 4));
    }

    void verifyDLL(DoublyLinkedList<Integer> dll, List<Integer> expectedListElements) {
        assertEquals(expectedListElements.get(expectedListElements.size() - 1), dll.getLastNode().getElement());
        assertEquals(expectedListElements.get(0), dll.getFirstNode().getElement());

        DLLNode<Integer> currentNode = dll.getFirstNode();
        for (Integer expectedListElement : expectedListElements) {
            assertNotNull(currentNode);
            assertEquals(expectedListElement, currentNode.getElement());
            currentNode = currentNode.getNext();
        }
        assertNull(currentNode.next);
    }
}
