package br.com.diogotorresdev.moneyapi.api.repository;

import br.com.diogotorresdev.moneyapi.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
