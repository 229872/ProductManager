import { AppBar, Toolbar, Typography } from "@mui/material";

export default function NavigationBar() {
  return (
    <AppBar>
      <Toolbar sx={{display: 'flex', justifyContent: 'space-between', bgcolor: 'gray'}}>
        <Typography variant='h4'>Product Manager</Typography>
      </Toolbar>
    </AppBar>
  )
}