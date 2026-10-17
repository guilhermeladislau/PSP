import { useState, type FormEvent } from 'react'
import type { Alimento } from './types/alimento'
import './App.css'

export default function App() {
  const [termoBusca, setTermoBusca] = useState('')
  const [alimentos, setAlimentos] = useState<Alimento[]>([])
  const [carregando, setCarregando] = useState(false)
  const [erro, setErro] = useState<string | null>(null)
  const [pesquisado, setPesquisado] = useState(false)

  async function handleBuscar(e: FormEvent) {
    e.preventDefault()
    const termoLimpo = termoBusca.trim()

    if (termoLimpo.length < 2) {
      setErro('Digite pelo menos 2 caracteres para pesquisar.')
      return
    }

    setCarregando(true)
    setErro(null)

    try {
      const response = await fetch(`/api/alimentos?busca=${encodeURIComponent(termoLimpo)}`)

      if (!response.ok) {
        if (response.status === 400) {
          setErro('Termo de busca inválido (mínimo de 2 caracteres).')
        } else {
          setErro(`Erro ao buscar alimentos (código ${response.status}).`)
        }
        setAlimentos([])
        return
      }

      const data: Alimento[] = await response.json()
      setAlimentos(data)
      setPesquisado(true)
    } catch {
      setErro('Não foi possível conectar ao servidor. Verifique se o backend está rodando na porta 8080.')
      setAlimentos([])
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="container">
      <header className="header">
        <h1>Plataforma Integrada</h1>
        <p className="subtitle">Módulo Alimentação — Catálogo de Alimentos</p>
      </header>

      <main className="content">
        <form onSubmit={handleBuscar} className="search-form">
          <label htmlFor="busca-input" className="sr-only">
            Buscar alimento
          </label>
          <input
            id="busca-input"
            type="search"
            value={termoBusca}
            onChange={(e) => setTermoBusca(e.target.value)}
            placeholder="Ex: arroz, feijão, frango..."
            className="search-input"
            disabled={carregando}
          />
          <button type="submit" className="search-button" disabled={carregando}>
            {carregando ? 'Buscando...' : 'Buscar'}
          </button>
        </form>

        {erro && <div className="alert alert-error">{erro}</div>}

        {pesquisado && !carregando && alimentos.length === 0 && !erro && (
          <div className="empty-state">
            Nenhum alimento encontrado para <strong>"{termoBusca.trim()}"</strong>.
          </div>
        )}

        {alimentos.length > 0 && (
          <section className="results-section">
            <h2>Resultados ({alimentos.length})</h2>
            <p className="results-hint">Valores nutricionais referem-se a 100 g de alimento.</p>
            <div className="table-responsive">
              <table className="alimentos-table">
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th className="num">Porção padrão</th>
                    <th className="num">Calorias</th>
                    <th className="num">Proteínas</th>
                    <th className="num">Carboidratos</th>
                    <th className="num">Gorduras</th>
                  </tr>
                </thead>
                <tbody>
                  {alimentos.map((alimento) => (
                    <tr key={alimento.id}>
                      <td className="alimento-nome">{alimento.nome}</td>
                      <td className="num">{alimento.porcaoPadraoGramas} g</td>
                      <td className="num">{alimento.caloriasKcal} kcal</td>
                      <td className="num">{alimento.proteinasG} g</td>
                      <td className="num">{alimento.carboidratosG} g</td>
                      <td className="num">{alimento.gordurasG} g</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        )}
      </main>
    </div>
  )
}
