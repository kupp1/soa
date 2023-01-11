const FLATS_SERVICE = "https://localhost:1443"
const AGENCY_SERVICE = "https://localhost:2443"


export const FLATS_API = `${FLATS_SERVICE}/api/v1/flat`
export const MIN_AREA_API = `${FLATS_SERVICE}/api/v1/flat/min-area`

export const WITH_BALCONY_API = `${AGENCY_SERVICE}/api/v1/agency/find-with-balcony`
export const ORDERED_TO_METRO = `${AGENCY_SERVICE}/api/v1/agency/get-ordered-by-time-to-metro`