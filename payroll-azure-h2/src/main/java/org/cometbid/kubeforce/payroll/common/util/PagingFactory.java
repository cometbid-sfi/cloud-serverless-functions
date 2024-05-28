/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.kubeforce.payroll.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import com.google.common.primitives.Ints;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.cometbid.kubeforce.payroll.employee.Employee;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class PagingFactory {

    private PagingFactory() {

    }

    public static Pageable CURRENT_PAGEABLE = PageRequest.of(PagingModel.DEFAULT.getPage(),
            PagingModel.DEFAULT.getSize(),
            Sort.by(Employee.DEFAULT_SORTFIELD));

    /**
     *
     * @param in
     * @param orders
     * @return
     */
    public static PageRequest createPageRequest(Optional<PagingModel> in, List<Order> orders) {
        PagingModel paging = in.orElse(PagingModel.DEFAULT);

        Sort sortby = orders.isEmpty() ? Sort.by(Employee.DEFAULT_SORTFIELD) : Sort.by(orders);

        return PageRequest.of(paging.getPage(),
                paging.getSize(),
                sortby);
    }

    /**
     *
     * @param map
     * @return
     */
    public static List<Order> createSortOrder(Map<String, Sort.Direction> map) {

        Set<Order> sortOrder = map.entrySet()
                .stream().filter(e -> e != null && StringUtils.isNotEmpty(e.getKey()))
                .map(e -> {
                    if (e.getValue() == null) {
                        e.setValue(Sort.Direction.ASC);
                    }
                    return e;
                })
                .map(entry -> new Order(entry.getValue(), entry.getKey()))
                .collect(Collectors.toSet());

        List<Order> orderList = new ArrayList<>();
        orderList.addAll(sortOrder);

        return orderList;
    }

    /**
     *
     * @param map
     * @return
     */
    public static Map<String, Sort.Direction> convertTo(Map<String, String> map) {

        return map.entrySet()
                .stream().filter(e -> e != null)
                .filter(PagingFactory::excludeFields)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> getSortDir(e.getValue()))
                );
    }

    private static boolean excludeFields(Map.Entry<String, String> e) {
        return !(e.getKey().equalsIgnoreCase("size")
                || e.getKey().equalsIgnoreCase("page"));
    }

    /**
     *
     * @param direction
     * @return
     */
    private static Sort.Direction getSortDir(String direction) {

        if (StringUtils.startsWithIgnoreCase(direction, "d")) {
            return Sort.Direction.DESC;
        } else {
            return Sort.Direction.ASC;
        }
    }

    /**
     *
     * @param map
     * @return
     */
    public static PagingModel createPagingModel(Map<String, String> map) {

        String page = map.getOrDefault("page", "");
        String size = map.getOrDefault("size", "");

        Integer pageNo = Ints.tryParse(page);
        Integer pageSize = Ints.tryParse(size);

        if (pageNo == null) {
            pageNo = PagingModel.DEFAULT.page();
        }

        if (pageSize == null) {
            pageSize = PagingModel.DEFAULT.size();
        }

        return PagingModel.of(pageNo, pageSize);
    }

    private static Map<String, String> extractSortParams(final Map<String, String> queryParams) {
        String sortParams = "";

        if (queryParams.containsKey("sort_by")) {
            sortParams = queryParams.get("sort_by");
        } else if (queryParams.containsKey("sortBy")) {
            sortParams = queryParams.get("sortBy");
        } else if (queryParams.containsKey("sortby")) {
            sortParams = queryParams.get("sortby");
        } else if (queryParams.containsKey("sort")) {
            sortParams = queryParams.get("sort");
        }

        String[] arrays = sortParams.split("[\\s\\[\\]\\{\\}()|@&?$+-]+");
        Set<String> deduplicatedFields = new HashSet<>(Arrays.asList(arrays));

        final Map<String, String> sortFields = new HashMap<>();
        deduplicatedFields.forEach(s -> {
            String[] arrayField = s.split("[\\.,]");
            List<String> list = Arrays.asList(arrayField);

            if (!list.isEmpty()) {
                String key = list.get(0);
                String value = null;
                if (list.size() > 1) {
                    value = list.get(1);
                }

                if (StringUtils.isNotBlank(key)) {
                    sortFields.put(key, value);
                }
            }
        });

        return sortFields;
    }

    /**
     *
     * @param queryParams
     * @return
     */
    public static Pageable preparePageRequest(Map<String, String> queryParams) {
        if (queryParams.isEmpty()) {
            log.info("No Request params: {}, returning default", queryParams);

            return CURRENT_PAGEABLE;
        }

        Map<String, Sort.Direction> map = PagingFactory.convertTo(extractSortParams(queryParams));
        log.info("Request params: {}", map);
        List<Sort.Order> list = PagingFactory.createSortOrder(map);

        PagingModel pagingModel = PagingFactory.createPagingModel(queryParams);

        return PagingFactory.createPageRequest(Optional.of(pagingModel), list);

        //return CURRENT_PAGEABLE;
    }
}
