export function parseFilters(filters) {
    return Object.keys(filters).map((key) => {
            if (key && filters[key]) {
                return { field: key, clause: `<${filters[key][0]}>${filters[key][1]}` }
            }
        }
    ).filter((element) => {
        return element !== undefined
    })
}

export function parseMapFilters(filtersMap) {
    return Array.from(filtersMap.keys()).map((key) => {
            if (key && filtersMap.get(key)) {
                return { field: key, clause:  `<${filtersMap.get(key)[0]}>${filtersMap.get(key)[1]}` }
            }
        }
    ).filter((element) => {
        return element !== undefined
    })
}

export function parseSorterToQueryParam(sort) {
    const order = sort.order === "ascend" ? "asc" : "desc";
    return { field: sort.columnKey, order: order}
}