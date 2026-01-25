package com.shop_app.category.validator;

import com.shop_app.category.CategoryRepository;
import com.shop_app.category.entity.Category;
import com.shop_app.shared.exceptions.DuplicateException;
import com.shop_app.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public Category validateAndGet(long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Category not found with id: " + id));
    }

    public void validateDuplicate(String name) {
        if (categoryRepository.existsByName(name))
            throw new DuplicateException(
                    "Duplicate name" + ": " + name);
    }

    public void validateInputListHasNotDuplicate(List<String> names) {
        if (!CollectionUtils.isEmpty(names) && names.size() > 1) {

            Set<String> uniqueNames = new HashSet<>();
            String duplicateName = names.stream().
                    filter(name -> !uniqueNames.add(name))
                    .distinct()
                    .collect(Collectors.joining(", "));

            if (!duplicateName.isEmpty())
                throw new DuplicateException(
                        "Duplicate name" + ": " + duplicateName);
        }
    }

    public void validateListIsNotDuplicateDB(List<String> names) {
        if (!CollectionUtils.isEmpty(names) && names.size() > 1) {

            List<String> duplicateNames = categoryRepository.findNamesInList(names);

            if (!duplicateNames.isEmpty()) {
                String foundNames = duplicateNames.stream().
                        collect(Collectors.joining(", "));

                throw new DuplicateException("Duplicate names: " + foundNames);
            }
        }
    }


}
