export interface Sample {
  id: number
  name: string
  description: string
  createdAt: string
  updatedAt: string
}

export interface ApiResponse<T> {
  data: T
  message?: string
}

export interface ErrorResponse {
  code: string
  message: string
}
