import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, TableSortLabel, Typography } from '@mui/material';
import axios from 'axios';
import { FormEvent, useEffect, useState } from "react";
import { API } from '../constants'

type Product = {
  id: number
  name: string
  price: number
}

type ProductResponse = {
  content: Product[]
  totalElements: number
}

export default function TableWithPaginationAndSorting() {
  const columns: string[] = ['ID', 'Name', 'Price']
  const rowsPerPageOptions = [5, 10, 15, 20]
  const margin = {margin: '14vh 10vw'}
  const headerStyling = {fontSize: 22, bgcolor: 'grey', color: 'white'}

  const [data, setData] = useState<Product[]>([])
  const [currentPage, setCurrentPage] = useState<number>(0)
  const [pageSize, setPageSize] = useState<number>(10)
  const [totalElements, setTotalElements] = useState<number>(0)
  const [sortBy, setSortBy] = useState<string>('id')
  const [direction, setDirection] = useState<'asc' | 'desc'>('asc')

  useEffect(() => {
    getData(currentPage, pageSize, "id", "asc")
  }, [])

  const getData = async (pageNr: number, pageSize: number, sortBy: string, direction: 'asc' | 'desc') => {
    try {
      const { data } = await axios.get<ProductResponse>(`${API}/products?page=${pageNr}&size=${pageSize}&sort=${sortBy},${direction}`)
      setData(data.content)
      setTotalElements(data.totalElements)
    } catch (e) {
      console.error(e)
    }
  }

  if (data.length == 0) {
    return <Typography sx={margin} variant='h3'>No records found</Typography>
  }

  const changePage = (event: any, page: number): void => {
    setCurrentPage(page)
    getData(page, pageSize, sortBy, direction)
  }

  const changeRowsPerPage = (event: any): void => {
    const newPageSize = event.target.value
    setPageSize(newPageSize)
    getData(currentPage, newPageSize, sortBy, direction)
  }

  const handleSortChange = (event: FormEvent<HTMLSpanElement>, column: string): void => {
    
    if (column === sortBy) {
      const newDirection = direction === 'asc' ? 'desc' : 'asc'
      changeDirection(newDirection)
    } else {
      changeSortBy(column)
    }
  }

  const changeDirection = (newDirection: 'asc' | 'desc') => {
    setDirection(newDirection)
    getData(currentPage, pageSize, sortBy, newDirection)
  }

  const changeSortBy = (newSortBy: string) => {
    setSortBy(newSortBy)
    getData(currentPage, pageSize, newSortBy, direction)
  }

  return (
    <Paper elevation={20} sx={margin}>
      <TableContainer sx={{maxHeight: '70vh'}}>
        <Table stickyHeader>
          <TableHead>
            <TableRow>
              {
                columns.map((v, key) => <TableCell key={key} sx={headerStyling}>
                  <TableSortLabel active={sortBy === v.toLowerCase()} direction={direction} onClick={e => handleSortChange(e, v.toLowerCase())}>
                    {v}
                  </TableSortLabel>
                </TableCell>)
              }
            </TableRow>
          </TableHead>

          <TableBody>
            {
              data && data
              .map(p => <TableRow key={p.id}>
                {
                  Object.values(p).map((v, key) => <TableCell key={key}>{v}</TableCell>)
                }
              </TableRow>)
            }
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination 
        component='div'
        page={currentPage}
        onPageChange={changePage}
        count={totalElements}
        rowsPerPage={pageSize}
        rowsPerPageOptions={rowsPerPageOptions}
        onRowsPerPageChange={changeRowsPerPage}
      />
    </Paper>
  )
}