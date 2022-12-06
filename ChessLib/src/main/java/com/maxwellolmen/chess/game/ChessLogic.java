package com.maxwellolmen.chess.game;

import java.util.ArrayList;
import java.util.List;

public class ChessLogic {

    private ChessBoard board;

    public ChessLogic(ChessBoard board) {
        this.board = board;
    }

    // 0: Legal, no specialty
    // 1: Legal, WQC
    // 2: Legal, WKC
    // 3: Legal, BQC
    // 4: Legal, BKC
    // 5: Legal, en passant
    // 6: Legal, promotion
    // 7: Illegal
    // 8: Legal, passantable
    public int isLegal(ChessPosition from, ChessPosition to) {
        ChessPiece a = board.getPiece(from);

        if (a.getType().isWhite() != board.getWhite()) {
            return 7;
        }

        if (a.getType() == ChessPieceType.WHITE_PAWN) {
            if (from.getRank() == 2 && to == ChessPosition.get(from, 0, 2) && board.getPiece(to) == null) {
                return 8;
            }

            if (to == ChessPosition.get(from, 0, 1) && board.getPiece(to) == null) {
                return to.getRank() == 8 ? 6 : 0;
            }

            if ((to == ChessPosition.get(from, 1, 1)
                        && ChessPosition.get(to, 0, -1) == board.getPassantable())
                    || (to == ChessPosition.get(from, -1, 1)
                        && ChessPosition.get(to, 0, -1) == board.getPassantable())) {
                return 5;
            }

            if (board.getPiece(to) != null
                    && (to == ChessPosition.get(from, 1, 1) || to == ChessPosition.get(from, -1, 1))) {
                return 0;
            }

            return 7;
        } else if (a.getType() == ChessPieceType.BLACK_PAWN) {
            if (from.getRank() == 7 && to == ChessPosition.get(from, 0, -2) && board.getPiece(to) == null) {
                return 8;
            }

            if (to == ChessPosition.get(from, 0, -1) && board.getPiece(to) == null) {
                return to.getRank() == 1 ? 6 : 0;
            }

            if ((to == ChessPosition.get(from, 1, -1)
                    && ChessPosition.get(to, 0, -1) == board.getPassantable())
                    || (to == ChessPosition.get(from, -1, -1)
                    && ChessPosition.get(to, 0, -1) == board.getPassantable())) {
                return 5;
            }

            if (board.getPiece(to) != null
                    && (to == ChessPosition.get(from, 1, -1) || to == ChessPosition.get(from, -1, -1))) {
                return 0;
            }

            return 7;
        } else {
            if (a.getType() == ChessPieceType.WHITE_KING) {
                if (board.isCastleable(1) && to == ChessPosition.C1
                        && board.getPiece(ChessPosition.D1) == null
                        && board.getPiece(ChessPosition.C1) == null
                        && board.getPiece(ChessPosition.B1) == null) {

                    return (board.getStatus(board.ghostMove(from, -1, 0).getPieces()) == 0
                            && board.getStatus(board.ghostMove(from, -2, 0).getPieces()) == 0) ? 1 : 7;
                }

                if (board.isCastleable(2) && to == ChessPosition.G1
                        && board.getPiece(ChessPosition.F1) == null
                        && board.getPiece(ChessPosition.G1) == null) {
                    return board.getStatus(board.ghostMove(from, 1, 0).getPieces()) == 0 ? 2 : 7;
                }
            } else if (a.getType() == ChessPieceType.BLACK_KING) {
                if (board.isCastleable(3) && to == ChessPosition.C8
                        && board.getPiece(ChessPosition.D8) == null
                        && board.getPiece(ChessPosition.C8) == null
                        && board.getPiece(ChessPosition.B8) == null) {

                    return (board.getStatus(board.ghostMove(from, 1, 0).getPieces()) == 0
                            && board.getStatus(board.ghostMove(from, 2, 0).getPieces()) == 0) ? 3 : 7;
                }

                if (board.isCastleable(4) && to == ChessPosition.G8
                        && board.getPiece(ChessPosition.F8) == null
                        && board.getPiece(ChessPosition.G8) == null) {
                    return board.getStatus(board.ghostMove(from, -1, 0).getPieces()) == 0 ? 4 : 7;
                }
            }

            List<ChessPosition> outlook = getOutlook(a);

            return outlook.contains(to) ? 0 : 7;
        }
    }

    public boolean isDefendingKing(ChessPiece piece) {
        return piece != null && piece.getType() == (board.getWhite() ? ChessPieceType.WHITE_KING : ChessPieceType.BLACK_KING);
    }

    public List<ChessPosition> getOutlook(ChessPiece piece) {
        return getOutlook(piece.getPosition(), piece.getType());
    }

    public void addPawnOutlook(ChessPosition pos, boolean white, List<ChessPosition> ds) {
        ChessPosition d1 = ChessPosition.get(pos, -1, white ? 1 : -1);
        ChessPosition d2 = ChessPosition.get(pos, 1, white ? 1 : -1);

        ds.add(d1);
        ds.add(d2);
    }

    public void addKnightOutlook(ChessPosition pos, List<ChessPosition> ds) {
        ChessPosition d1, d2, d3, d4, d5, d6, d7, d8;
        d1 = ChessPosition.get(pos, -1, 2);
        d2 = ChessPosition.get(pos, -1, -2);
        d3 = ChessPosition.get(pos, -2, 1);
        d4 = ChessPosition.get(pos, -2, -1);
        d5 = ChessPosition.get(pos, 1, 2);
        d6 = ChessPosition.get(pos, 1, -2);
        d7 = ChessPosition.get(pos, 2, 1);
        d8 = ChessPosition.get(pos, 2, -1);

        ds.add(d1);
        ds.add(d2);
        ds.add(d3);
        ds.add(d4);
        ds.add(d5);
        ds.add(d6);
        ds.add(d7);
        ds.add(d8);
    }

    public void addBishopOutlook(ChessPosition pos, List<ChessPosition> ds) {
        int f = 0, r = 0, i = 1;

        while (true) {
            if (i == 1) {
                f++;
                r++;
            } else if (i == 2) {
                f++;
                r--;
            } else if (i == 3) {
                f--;
                r++;
            } else {
                f--;
                r--;
            }

            ChessPosition p = ChessPosition.get(pos, f, r);

            if (p == null) {
                if (i < 4) {
                    i++;
                    f = 0;
                    r = 0;

                    continue;
                }

                break;
            }

            ChessPiece d = board.getPiece(p);

            ds.add(p);

            if (d != null) {
                if (i < 4) {
                    i++;
                    f = 0;
                    r = 0;
                } else {
                    break;
                }
            }
        }
    }

    public void addRookOutlook(ChessPosition pos, List<ChessPosition> ds) {
        int f = 0, r = 0, i = 1;

        while (true) {
            if (i == 1) {
                f++;
            } else if (i == 2) {
                f--;
            } else if (i == 3) {
                r++;
            } else {
                r--;
            }

            ChessPosition p = ChessPosition.get(pos, f, r);

            if (p == null) {
                if (i < 4) {
                    i++;
                    f = 0;
                    r = 0;

                    continue;
                }

                break;
            }

            ChessPiece d = board.getPiece(p);

            ds.add(p);

            if (d != null) {
                if (i < 4) {
                    i++;
                    f = 0;
                    r = 0;
                } else {
                    break;
                }
            }
        }
    }

    public void addKingOutlook(ChessPosition pos, List<ChessPosition> ds) {
        ChessPosition d1, d2, d3, d4, d5, d6, d7, d8;
        d1 = ChessPosition.get(pos, -1, -1);
        d2 = ChessPosition.get(pos, -1, 0);
        d3 = ChessPosition.get(pos, -1, 1);
        d4 = ChessPosition.get(pos, 0, -1);
        d5 = ChessPosition.get(pos, 0, 1);
        d6 = ChessPosition.get(pos, 1, -1);
        d7 = ChessPosition.get(pos, 1, 0);
        d8 = ChessPosition.get(pos, 1, 1);

        if (d1 != null) ds.add(d1);
        if (d2 != null) ds.add(d2);
        if (d3 != null) ds.add(d3);
        if (d4 != null) ds.add(d4);
        if (d5 != null) ds.add(d5);
        if (d6 != null) ds.add(d6);
        if (d7 != null) ds.add(d7);
        if (d8 != null) ds.add(d8);
    }

    public List<ChessPosition> getOutlook(ChessPosition pos, ChessPieceType type) {
        ArrayList<ChessPosition> ds = new ArrayList<>();

        if (type == ChessPieceType.WHITE_PAWN || type == ChessPieceType.BLACK_PAWN) {
            addPawnOutlook(pos, board.getWhite(), ds);
        } else if (type == ChessPieceType.WHITE_KNIGHT || type == ChessPieceType.BLACK_KNIGHT) {
            addKnightOutlook(pos, ds);
        } else if (type == ChessPieceType.WHITE_BISHOP || type == ChessPieceType.BLACK_BISHOP) {
            addBishopOutlook(pos, ds);
        } else if (type == ChessPieceType.WHITE_ROOK || type == ChessPieceType.BLACK_ROOK) {
            addRookOutlook(pos, ds);
        } else if (type == ChessPieceType.WHITE_QUEEN || type == ChessPieceType.BLACK_QUEEN) {
            addRookOutlook(pos, ds);
            addBishopOutlook(pos, ds);
        } else if (type == ChessPieceType.WHITE_KING || type == ChessPieceType.BLACK_KING) {
            addKingOutlook(pos, ds);
        }

        for (ChessPosition d : (ArrayList<ChessPosition>) ds.clone()) {
            if (board.getPiece(d) != null && board.getPiece(pos) != null && board.getPiece(d).getType() == board.getPiece(pos).getType()) {
                ds.remove(d);
            }
        }

        return ds;
    }

    // 0: No check
    // 1: Check
    // 2: Checkmate
    public int isChecking(ChessPiece piece, boolean ignoreMate) {
        if (piece == null) {
            return 0;
        }

        List<ChessPosition> outlook = getOutlook(piece);

        for (ChessPosition pos : outlook) {
            if (pos == null) {
                continue;
            }

            if (board.getPiece(pos) == null) {
                continue;
            }

            if (isDefendingKing(board.getPiece(pos)) && piece.getType().isWhite() != board.getPiece(pos).getType().isWhite()) {
                if (ignoreMate) {
                    return 1;
                }

                return isCheckmate(board.getPiece(pos)) ? 2 : 1;
            }
        }

        return 0;
    }

    private boolean isCheckmate(ChessPiece king) {
        for (ChessPosition pos : getOutlook(king)) {
            if (board.getStatus(board.ghostMove(king.getPosition(), pos).getPieces(), true) == 0) {
                return false;
            }
        }

        for (ChessPiece piece : board.getPieces()) {
            if (piece.getType().isWhite() == king.getType().isWhite()) {
                for (ChessPosition pos : getOutlook(piece)) {
                    if (board.getStatus(board.ghostMove(piece.getPosition(), pos).getPieces(), true) == 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}