package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("userA", "대전", "왕구", "123-12");
            em.persist(member);

            Book bookA = createBook("BookA", 10000, 100);
            em.persist(bookA);

            Book bookB = createBook("BookB", 20000, 100);
            em.persist(bookB);

            OrderItem orderItem1 = OrderItem.createOrderItem(bookA, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(bookB, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "서울", "중왕구", "123-1112");
            em.persist(member);

            Book bookA = createBook("BookA2", 20000, 200);
            em.persist(bookA);

            Book bookB = createBook("BookB2", 40000, 300);
            em.persist(bookB);

            OrderItem orderItem1 = OrderItem.createOrderItem(bookA, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(bookB, 40000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private static Book createBook(String name, int price, int stockQuantity) {
            Book bookA = new Book();
            bookA.setName(name);
            bookA.setPrice(price);
            bookA.setStockQuantity(stockQuantity);
            return bookA;
        }

        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
    }

}


