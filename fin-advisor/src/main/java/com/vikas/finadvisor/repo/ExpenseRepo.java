package com.vikas.finadvisor.repo;

import com.vikas.finadvisor.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {
    // You can add custom query methods later if needed
}
